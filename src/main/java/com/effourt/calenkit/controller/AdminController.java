package com.effourt.calenkit.controller;

import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final MemberRepository memberRepository;
    private final HttpSession session;

    // Admin 페이지 이동
    @GetMapping
    public String admin() {
        return "admin/admin";
    }

    // 로그인 멤버 제외한 멤버 검색(SELECT)
    // keyword(Form) 통해 검색 후 멤버 리스트 출력
    @GetMapping ("/list")
    @ResponseBody
    public List<Member> adminIdList(@RequestParam(required = false) String keyword) {
        String loginId=(String)session.getAttribute("loginId");
        List<Member> memberList = memberRepository.findAllByMemId(keyword);

        //로그인아이디 출력 제외
        memberList.removeIf(member -> member.getMemId().equals(loginId));

        return memberList;
    }

    // 멤버 권한 변경(UPDATE)
    // 셀렉트박스로 변경된 memId,memStatus 전달받아 상태 변경
    @PatchMapping("/update")
    public String adminModify(@RequestParam("selectedValue") Integer memStatus, String memId) {
        Member member = memberRepository.findByMemId(memId);
        member.setMemStatus(memStatus);
        adminService.modifyStatus(member);
        return "redirect:/admin";
    }

    // 멤버 삭제(DELETE)
    // 1.체크박스가 선택된 memIdList를 가져옴
    // 2.for문을 memId 객체 분리
    // 3.memId를 통해  Schedule 검색 후 모든 scNo 가져옴
    // 4.scNo와 memId를 통해 Team 검색 후 teamLevel=9,그 외 경우 분류
    // 5.teamLevel=9일 경우는 scNo를 통해 TEAM MEMBER 검색 후 ALARM,TEAM정보 삭제 후 SCHEDULE,MEMBER 정보 삭제
    // 6.teamLevel=0,1일 경우는 나의 ALARM,TEAM,SCHEDULE,MEMBER 정보 차례대로 삭제
    @DeleteMapping(value ="/delete")
    public String adminDelete(@RequestParam("memIdList") List<String> memIdList) {
        //체크 된 멤버리스트 삭제하기 위해 for문 사용.
        for (String originMemId : memIdList) {
            //리스트로 객체 받아올 경우 [,],"가 포함되어있는데 이걸 제거하기 위해 사용함.
            String originalString =originMemId.toString();
            String memId = originalString.replaceAll("[\\[\\]\",]", "");
            adminService.removeMember(memId);
        }
        return "redirect:/admin";
    }

}
