package com.effourt.calenkit.controller;

import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.dto.EmailMessage;
import com.effourt.calenkit.dto.SocialRequest;
import com.effourt.calenkit.exception.MemberNotFoundException;
import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.service.AdminService;
import com.effourt.calenkit.service.JoinService;
import com.effourt.calenkit.service.LoginService;
import com.effourt.calenkit.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

//@Controller
@RequiredArgsConstructor
public class MemberController {

    private final LoginService loginService;
    private final JoinService joinService;
    private MyPageService myPageService;
    private AdminService adminService;
    private MemberRepository memberRepository;

    //DB에서 아이디 체크
    //아이디 존재 O, 비밀번호 O : PASSWORD_LOGIN
    //아이디 존재 O, 비밀번호 X : CODE_LOGIN
    //아이디 존재 X, 비밀번호 X : JOIN_LOGIN
    @PostMapping("")
    @ResponseBody
    public String checkId(String id, HttpSession session) {
        String loginType = loginService.checkMember(id);
        EmailMessage emailMessage = EmailMessage.builder()
                .recipient("")
                .subject("")
                .message("")
                .build();
        if (loginType.equals("CODE_LOGIN") || loginType.equals("JOIN_LOGIN")) {
            loginService.sendCode(id, emailMessage, session);
        }
        return loginType;
    }

    
    /** 로그인 */
    //패스워드로 로그인
    @PostMapping("")
    public String loginByPassword(@ModelAttribute Member member, HttpSession session) {
        Member findMember = loginService.getMemberById(member.getMemId());
        if (findMember.getMemPw().equals(member.getMemPw())) {
            session.setAttribute("loginId", member.getMemId());
        }
        return "";
    }
    
    //로그인 코드로 로그인
    @PostMapping("")
    public String loginByCode(String id, String code, HttpSession session) {
        String loginCode = (String) session.getAttribute(code);
        if (loginCode.equals(id + "ACCESS")) {
            session.setAttribute("loginId", id);
        }
        return "";
    }

    //소셜 로그인
    @PostMapping("/kakao-login")
    @ResponseBody
    public String loginByKakao(@RequestBody SocialRequest socialRequest) {

        return "";
    }

    //회원가입 후 이메일 로그인
    @PostMapping("")
    public String loginByJoin(String id, String code, HttpSession session, Model model) {
        String joinCode = (String) session.getAttribute(code);
        if (joinCode.equals(id + "ACCESS")) {
            model.addAttribute("id", id);
        }
        return "";
    }

    @GetMapping("")
    public String logout(HttpSession session) {
        session.invalidate();
        return "";
    }

    /** 회원가입 */
    // 이메일 회원가입
    // 입력 데이터 : 아이디(이메일), 비밀번호, 닉네임, 프로필 이미지(선택)
    @PostMapping("")
    public String join(@ModelAttribute Member member) {
        joinService.joinByEmail(member);
        return "";
    }

    /** 관리자 */
    // Admin
    // 전체 멤버 리스트 출력 & 멤버 아이디 검색 후 리스트 출력(GET)
    // 전체 리스트 출력 시 Json형식의 text로 List객체 전달
    @GetMapping(value = "/admin_memberList")
    @ResponseBody
    public List<Member> MemberIdList(@RequestParam String keyword) {
        List<Member> memberList=memberRepository.findAllByMemId(keyword);

        return memberList;
    }

    // Admin
    // 멤버 상태변경(PATCH)
    // 로그인세션에서 아이디값을 전달받아 member_list 페이지로 이동
    @PatchMapping(value ="/admin_statusModify")
    public String MemberModify(@RequestAttribute Member member,@RequestParam Integer keyword) throws MemberNotFoundException {
        member.setMemStatus(keyword);
        return "admin/member_list";
    }

    // Admin
    // 멤버 삭제(PATCH)
    // 로그인세션에서 아이디값을 전달받아 member_list 페이지로 이동
    @DeleteMapping(value ="/admin_delete")
    public String MemberModify(@RequestAttribute Member member) throws MemberNotFoundException {
        adminService.removeMember(member);
        return "admin/member_list";
    }

    /** 마이 페이지 */
    // MyPage
    // 멤버 닉네임 검색 후 중복 확인(GET)
    @GetMapping(value = "/myPage_name")
    @ResponseBody
    public String MemberName(@RequestParam String memName) {
        Member member=memberRepository.findByMemName(memName);
        return member.getMemName();
    }

    // MyPage
    // 멤버 정보변경(GET)
    // 로그인세션에서 아이디값을 전달받아 member_modify 페이지로 이동
    @GetMapping(value ="/myPage_modify")
    public String MemberModify(Model model, HttpSession session) throws MemberNotFoundException {
        Member member=(Member)session.getAttribute("loginMember");
        model.addAttribute("member", memberRepository.findByMemId(member.getMemId()));
        return "member/member_modify";
    }

    // MyPage
    // 멤버 정보변경(PATCH)
    // Form태그를 통해 전달받은 값들을 member 객체에 update 후 member_modify 페이지로 이동
    @PatchMapping(value ="/myPage_modify")
    public String MemberModify(HttpSession session) throws MemberNotFoundException {
        Member member=(Member)session.getAttribute("loginMember");
        myPageService.modifyMe(member);
        return "redirect:/member_modify";
    }


    // MyPage
    // 멤버 비밀번호 정보변경(PATCH)
    // 로그인세션에서 아이디값을 전달받아 member_modify 페이지로 이동처리.
    @PatchMapping(value ="/myPage_pwModify")
    public String MemberPwModify(Model model, HttpSession session) throws MemberNotFoundException {
        Member member=(Member)session.getAttribute("loginMember");
        myPageService.modifyPassword(member);
        return "member/member_pwModify";
    }

}
