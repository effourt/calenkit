package com.effourt.calenkit.controller;

import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.service.MyPageService;
import com.effourt.calenkit.util.ImageUpload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MypageController {
    private final MyPageService myPageService;
    private final MemberRepository memberRepository;
    private final ImageUpload imageUploadService;
    private final PasswordEncoder passwordEncoder;
    private final HttpSession session;
    /** 마이 페이지 (/members/myPage)*/
    //MyPage 이동
    @GetMapping(value = "/members/myPage")
    public String myPage(Model model) {
        String loginId=(String)session.getAttribute("loginId");
        Member loginMember=memberRepository.findByMemId(loginId);
        model.addAttribute("loginMember",loginMember);
        return "member/myPage";
    }


    /** 마이 페이지 (/members/myPage)*/
    // memImage Ajax 유효성 Ajax 검사&변경(UPDATE)
    // 1.Ajax를 통해 파일의 이미지 유효성 확인
    // 2.파일 업로드 처리 후 파일 미리보기 처리
    @PostMapping(value = "/members/myPage/modify_image")
    public String saveImage(@RequestParam MultipartFile memImage) throws IOException {
        String loginId = (String) session.getAttribute("loginId");
        Member loginMember = memberRepository.findByMemId(loginId);
        // 이미지 업로드 후 파일명 반환
        String fileName = imageUploadService.uploadImage(memImage);
        // Member 객체에 이미지 파일명 저장
        loginMember.setMemImage(fileName);
        // Member 객체를 인자로 받는 modifyMe() 메소드 호출
        myPageService.modifyMe(loginMember);

        return "redirect:";


    }


    /** 마이 페이지 (/members/myPage)*/
    // memName 유효성&중복 Ajax 검사(GET)
    // 1.멤버 닉네임 유효성 확인 (틀릴 경우 cnt = 2 반환)
    // 2.유효성 확인 후 멤버 닉네임 검색 후 중복 확인 (일치 cnt=1, 불일치 cnt=0)
    @GetMapping(value = "/members/myPage/nameCheck")
    @ResponseBody
    public int nameCheck(String memName) {
        int cnt=0;
        if (memName.matches("^[a-zA-Z가-힣0-9]{2,10}$")) {
            cnt = memberRepository.findByMemName(memName);
            return cnt;
        } else {
            cnt = 2;
            return cnt;
        }
    }

    /** 마이 페이지 (/members/myPage)*/
    // memName Ajax 변경(UPDATE)
    @PostMapping(value = "/members/myPage/modify_name")
    public String saveName(@RequestParam String memName){
        String loginId=(String)session.getAttribute("loginId");
        Member loginMember=memberRepository.findByMemId(loginId);
        loginMember.setMemName(memName);
        myPageService.modifyMe(loginMember);
        return "redirect:/";
    }


    /** 마이 페이지 패스워드 변경 (/members/myPageModify)*/
    // memPw 변경 페이지 이동(Get)
    @GetMapping(value = "/members/myPageModify/pwModify")
    public String savePw(Model model){
        String loginId=(String)session.getAttribute("loginId");
        Member loginMember=memberRepository.findByMemId(loginId);
        model.addAttribute("loginMember",loginMember);
        return "member/myPageModify";
    }

    /** 마이 페이지 패스워드 변경 (/members/myPageModify)*/
    // memPw 변경(Post)
    // 1.현재 비밀번호가 null인 경우 검증없이 비밀번호 등록
    // 2.현재 비밀번호가 존재 할 경우 passwordEncoder 검증 후 비밀번호 등록
    @PostMapping(value = "/members/myPageModify/pwModify")
    public String myPagePwModify(@RequestParam String memPw,@RequestParam String password1) {
        String loginId=(String)session.getAttribute("loginId");
        Member loginMember=memberRepository.findByMemId(loginId);

        //비밀번호 없을 경우
        if(loginMember.getMemPw()==null){
            myPageService.modifyPassword(loginMember,password1);
            return "member/endPage";
        }
        //기존 비밀번호 있을 경우
        else {
            if (passwordEncoder.matches(memPw, loginMember.getMemPw())) {
                myPageService.modifyPassword(loginMember, password1);
                return "member/endPage";
            } else {
                return "member/myPage";
            }
        }
    }


    /** 마이 페이지 패스워드 변경 (/members/myPageModify)*/
    // memPw 일치 여부 Ajax 검사(GET)
    // 현재 비밀번호 일치 여부 확인 (일치 cnt=1, 불일치 cnt=0)
    @GetMapping(value = "/members/myPageModify/pwCheck")
    @ResponseBody
    public int pwCheck(@RequestParam String memPw) {
        String loginId=(String)session.getAttribute("loginId");
        Member loginMember=memberRepository.findByMemId(loginId);
        int cnt=0;
        if(memPw==null){
        }
        if (passwordEncoder.matches(memPw,loginMember.getMemPw())){
            cnt++;
            return cnt;
        }
        return cnt;
    }

    /** 마이 페이지 패스워드 변경 (/members/myPageModify)*/
    // password1 유효성 검사 여부 Ajax 검사(GET)
    // 유효성 확인 (일치 cnt=1, 불일치 cnt=0)
    @GetMapping(value = "/members/myPageModify/passwordCheck2")
    @ResponseBody
    public int passwordCheck2(String password1) {
        int cnt = 0;
        if(password1.matches("(?=.*\\d)(?=.*[a-z])(?=.*[!@#])[\\da-zA-Z!@#]{8,15}")) {
            cnt++;
            return cnt; //1 출력
        }
        return cnt; //0 출력
    }

    /** 마이 페이지 패스워드 변경 (/members/myPageModify)*/
    // password1,password2 일치 여부 Ajax 검사(GET)
    // 현재 비밀번호 일치 여부 확인 (일치 cnt=1, 불일치 cnt=0)
    @GetMapping(value = "/members/myPageModify/passwordCheck")
    @ResponseBody
    public int passwordCheck(String password1, String password2,String memPw) {
        String loginId=(String)session.getAttribute("loginId");
        Member loginMember=memberRepository.findByMemId(loginId);

        int cnt = 0;
        if(password1.matches("(?=.*\\d)(?=.*[a-z])(?=.*[!-*])[\\da-zA-Z!@#]{8,15}")) {
            System.out.println("cnt1="+cnt);
            if(memPw!=null || memPw!="") {
                System.out.println("memPw1="+memPw);
                if (passwordEncoder.matches(memPw, loginMember.getMemPw()) && password2.equals(password1))
                {
                    cnt++; //
                    System.out.println("cnt2=" + cnt);
                    return cnt;
                }//1 출력 password2와 password1이 일치할 경우
            }
            if(memPw==null || memPw==""){
                System.out.println("memPw2="+memPw);
                if(password2.equals(password1))
                {
                    cnt++; //
                    System.out.println("cnt2=" + cnt);
                    return cnt;
                }
            }
        }
        return cnt; //0 출력
    }

    /** 마이 페이지 삭제 (/members/myPageDelete)*/
    //myPageDelete 이동(GET)
    @GetMapping(value = "/members/myPageDelete/delete")
    public String myPageDelete(){
        return "member/myPageDelete";
    }

    /** 마이 페이지 삭제 (/members/myPageDelete)*/
    // 아이디 일치 Ajax 검사(GET)
    // 일치 여부 확인 (일치 cnt=1, 불일치 cnt=0)
    @GetMapping(value = "/members/myPageDelete/idCheck")
    @ResponseBody
    public int idCheck(String memId) {
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

    /** 마이 페이지 삭제 (/members/myPageDelete)*/
    // 멤버 상태 변경(Post)
    // 아이디값 비교 후 멤버 상태 0 변경
    @PostMapping(value = "/members/myPageDelete/delete")
    public String myPageDelete(String memId) {
        Integer memStatus=0;
        String loginId=(String)session.getAttribute("loginId");
        Member member=memberRepository.findByMemId(loginId);
        if(member.getMemId().equals(memId)) {
            member.setMemStatus(memStatus);
            myPageService.removeMe(member);
            session.removeAttribute("loginId");
            return "member/endPage";
        }
        else{
            return "login";
        }
    }

}
