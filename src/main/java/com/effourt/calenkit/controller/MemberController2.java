package com.effourt.calenkit.controller;

import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.exception.MemberNotFoundException;
import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.service.AdminService;
import com.effourt.calenkit.service.MyPageService;
import com.effourt.calenkit.util.ImageUpload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController2 {
    private final MyPageService myPageService;
    private final AdminService adminService;
    private final MemberRepository memberRepository;
    private final ImageUpload imageUploadService;

    /** 관리자 */
    // Admin
    @GetMapping(value = "/admin")
    public String AdminList(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("keyword", keyword);
        return "member/admin";
    }




    // Admin
    // 전체 멤버 리스트 출력 & 멤버 아이디 검색 후 리스트 출력(GET)
    // 전체 리스트 출력 시 Json형식의 text로 List객체 전달
    @GetMapping(value = "/admin_memberList")
    @ResponseBody
    public List<Member> AdminIdList(@RequestParam(required = false) String keyword) {
        List<Member> memberList = memberRepository.findAllByMemId(keyword);
        return memberList;
    }


    // Admin
    // 멤버 상태변경(PATCH)
    //  member_list 페이지로 이동
    @PatchMapping(value ="/admin_statusModify")
    public String AdminModify(@ModelAttribute Member member) throws MemberNotFoundException {
        Member selectMember=memberRepository.findByMemId(member.getMemId());
        return "adminPage";
    }

    // Admin
    // 멤버 삭제(DELETE)
    // 로그인세션에서 아이디값을 전달받아 member_list 페이지로 이동
    @DeleteMapping(value ="/admin_delete")
    public String AdminDelete(@ModelAttribute Member member) throws MemberNotFoundException {
        adminService.removeMember(member);
        return "adminPage";
    }

    /** 마이 페이지 */


    //MyPage 이동
    @GetMapping(value = "/myPage")
    public String MyPage(HttpSession session,Model model) {
        String loginId=(String)session.getAttribute("loginId");
        Member loginMember=memberRepository.findByMemId(loginId);
        model.addAttribute("memName",loginMember.getMemName());
        model.addAttribute("memImage",loginMember.getMemImage());
        System.out.println(loginMember.getMemImage());
        return "member/myPage";
    }
    @GetMapping(value = "/myPage_delete")
    public String myPageDelete() {

        return "member/myPageDelete";
    }
    @GetMapping(value = "/myPage_pwModify")
    public String myPagePwModify() {
        return "member/mypageModify";
    }


    // MyPage
    // 멤버 닉네임 검색 후 중복 확인(GET)
    // Ajax 처리를 위해 닉네임 중복 갯수 반환
    @GetMapping("/nameCheck")
    @ResponseBody
    public int nameCheck(@RequestParam("memName") String memName) {
        int cnt = memberRepository.findByMemName(memName);
        return cnt;

    }
    // MyPage
    // 아이디 검색 후 중복 확인(GET)
    // Ajax 처리를 위해 아이디 중복 갯수 반환
    @GetMapping("/idCheck")
    @ResponseBody
    public int idCheck(@RequestParam("memId") String memId,HttpSession session) {
        String loginId=(String)session.getAttribute("loginId");
        int cnt=0;
        if(loginId.equals(memId)){
        Member member = memberRepository.findByMemId(memId);
        if(member!=null){
            cnt++;
            return cnt;
        }}

        return cnt;
    }

    // MyPage
    // 아이디 검색 후 중복 확인(GET)
    // Ajax 처리를 위해 비밀번호 중복 갯수 반환
    @GetMapping("/passwordCheck")
    @ResponseBody
    public int passwordCheck(String password1,String password2) {
        int cnt=0;
        if (password1.equals(password2)){
            cnt++;
            return cnt;
        }
        return cnt;
    }
    @PostMapping("/modify_image")
    public String saveMember(@RequestParam(value="memImage",required = false ) MultipartFile memImage,HttpSession session) throws MemberNotFoundException, IOException {
        String loginId=(String)session.getAttribute("loginId");
        Member loginMember=memberRepository.findByMemId(loginId);
        // 이미지 업로드 후 파일명 반환
        String fileName = imageUploadService.uploadImage(memImage);
        // Member 객체에 이미지 파일명 저장
        loginMember.setMemImage(fileName);
        // Member 객체를 인자로 받는 modifyMe() 메소드 호출
        myPageService.modifyMe(loginMember);

        return "redirect:/myPage";
    }

    @PostMapping("/modify_name")
    public String Member(@RequestParam("memName") String memName,HttpSession session) throws MemberNotFoundException, IOException {
        String loginId=(String)session.getAttribute("loginId");
        Member loginMember=memberRepository.findByMemId(loginId);
        // 닉네임 업로드
        loginMember.setMemName(memName);
        // Member 객체를 인자로 받는 modifyMe() 메소드 호출
        myPageService.modifyMe(loginMember);
        return "redirect:/myPage";
    }



    // MyPage
    // 멤버 비밀번호 정보변경(Put)
    // 로그인세션에서 아이디값을 전달받아 member_pwModify 페이지로 이동처리.
    @PostMapping(value ="/myPage_pwModify")
    public String MyPagePwModify(HttpSession session,String memPw,String password1) throws MemberNotFoundException {
        String loginId=(String)session.getAttribute("loginId");
        Member loginMember=memberRepository.findByMemId(loginId);
        System.out.println(BCrypt.checkpw(memPw, loginMember.getMemPw()));
        if(BCrypt.checkpw(memPw, loginMember.getMemPw())){
        //if (loginMember.getMemPw().equals(memPw))
            myPageService.modifyPassword(loginMember,password1);
            return "member/myPage";
        }
        else {
            return "member/myPage";
        }

    }
    // MyPage
    // 멤버 상태 변경(Put)
    // 로그인세션에서 아이디값을 전달받아 member_delete 페이지로 이동처리.
    @PostMapping(value ="/myPage_delete")
    public String MyPageDelete(HttpSession session,String memId) throws MemberNotFoundException {
        String loginId=(String)session.getAttribute("loginId");
        Member loginMember=memberRepository.findByMemId(loginId);
        Integer memStatus=0;
        if(loginMember.getMemId().equals(memId)) {
            myPageService.removeMe(loginMember, memStatus);
            System.out.println("memStatus"+memStatus+loginMember.getMemId());

            return "member/myPage";
        }
        else{
            return "/myPage_delete";
        }
    }
}
