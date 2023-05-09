package com.effourt.calenkit.controller;

import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.exception.MemberNotFoundException;
import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.service.AdminService;
import com.effourt.calenkit.service.MyPageService;
import com.effourt.calenkit.util.ImageUpload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private final PasswordEncoder passwordEncoder;
    private final HttpSession session;

    /** 관리자 */
    // Admin
    @GetMapping(value = "/admin")
    public String Admin(@RequestParam(required = false) String keyword, Model model) {
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



    @GetMapping(value = "/admin_modifyMember")
    public String AdminModify(@RequestParam("selectedValue") Integer memStatus, String memId) {
        Member member = memberRepository.findByMemId(memId);
        member.setMemStatus(memStatus);
        adminService.modifyStatus(member);
        return "member/admin";
    }

    // Admin
    // 멤버 삭제(DELETE)
    // 로그인세션에서 아이디값을 전달받아 member_list 페이지로 이동
    @PostMapping(value ="/admin_delete")
    public String adminDelete(@RequestParam("memIdList") List<String> memIdList) throws MemberNotFoundException {
        //체크 된 멤버리스트 삭제하기 위해 for문 사용.
        for (String originMemId : memIdList) {
            //리스트로 객체 받아올 경우 [,],"가 포함되어있는데 이걸 제거하기 위해 사용함.
            String originalString =originMemId.toString();
            String memId = originalString.replaceAll("[\\[\\]\",]", "");
            //본인 계정 삭제 불가능
            if(memId.equals(session.getAttribute("loginId"))){
                return "member/admin";
            }
            else {
                adminService.removeMember(memId);
            }
       }
        return "member/admin";
    }

    /** 마이 페이지 */


    //MyPage 이동
    @GetMapping(value = "/myPage")
    public String MyPage(Model model) {
        String loginId=(String)session.getAttribute("loginId");
        Member loginMember=memberRepository.findByMemId(loginId);
        model.addAttribute("loginMember",loginMember);
        return "member/myPage";
    }

    // MyPage
    // 멤버 닉네임 검색 후 중복 확인(GET)
    // Ajax 처리를 위해 닉네임 중복 갯수 반환
    @GetMapping("/nameCheck")
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
    // MyPage
    // 아이디 검색 후 중복 확인(GET)
    // Ajax 처리를 위해 아이디 중복 갯수 반환
    @GetMapping("/idCheck")
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


    @PostMapping("/modify_image")
    public String saveImage(@RequestParam(required = false ) MultipartFile memImage) throws MemberNotFoundException, IOException {
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

    // MyPage
    // 닉네임 검색 후 중복 확인(GET)
    // 형식에 맞거나 사용하지 않는 닉네임 Ajax처리.
    @PostMapping("/modify_name")
    public String saveName(String memName) throws MemberNotFoundException, IOException {
        String loginId=(String)session.getAttribute("loginId");
        Member loginMember=memberRepository.findByMemId(loginId);
        // 닉네임 업로드
        loginMember.setMemName(memName);
        // Member 객체를 인자로 받는 modifyMe() 메소드 호출
        myPageService.modifyMe(loginMember);
        return "redirect:/myPage";
    }



    // MyPage
    // 아이디 검색 후 중복 확인(GET)
    // 로그인 계정 비밀번호와 비밀번호 확인 Ajax 비교
    @GetMapping("/pwCheck")
    @ResponseBody
    public int pwCheck(String memPw) {
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


    // MyPage
    // 아이디 검색 후 중복 확인(GET)
    // Ajax 처리를 위해 비밀번호 중복 갯수 반환
    @GetMapping("/passwordCheck")
    @ResponseBody
    public int passwordCheck(String password1, String password2) {
        int cnt = 0;
        if(password1.matches("(?=.*\\d)(?=.*[a-z])(?=.*[!@#])[\\da-zA-Z!@#]{8,15}")) {
            System.out.println("cnt1="+cnt);
            if (password2.equals(password1)) {
                cnt++; //
                System.out.println("cnt2="+cnt);
                return cnt; //1 출력 password2와 password1이 일치할 경우
            }
        }
        return cnt; //0 출력
    }
    @GetMapping("/passwordCheck2")
    @ResponseBody
    public int passwordCheck2(String password1) {
        int cnt = 0;
        if(password1.matches("(?=.*\\d)(?=.*[a-z])(?=.*[!@#])[\\da-zA-Z!@#]{8,15}")) {
            System.out.println("cnt1="+cnt);
                cnt++;
                System.out.println("cnt2="+cnt);
                return cnt; //1 출력 password2와 password1이 일치할 경우
        }
        return cnt; //0 출력
    }
    // MyPage
    // 멤버 비밀번호 정보변경(Get)
    @GetMapping(value ="/myPage_pwModify")
    public String savePw(Model model){
        String loginId=(String)session.getAttribute("loginId");
        Member loginMember=memberRepository.findByMemId(loginId);
        System.out.print("loginId="+loginId);
        System.out.print("loginId="+loginMember.getMemPw());
        model.addAttribute("loginMember",loginMember);
        return "member/myPageModify";
    }

    // MyPage
    // 멤버 비밀번호 정보변경(Put)
    // 로그인세션에서 아이디값을 전달받아 member_pwModify 페이지로 이동처리.
    @PostMapping(value ="/myPage_pwModify")
    public String MyPagePwModify(String memPw,String password1) throws MemberNotFoundException {
        String loginId=(String)session.getAttribute("loginId");
        Member loginMember=memberRepository.findByMemId(loginId);
        System.out.println(loginMember.getMemPw()+password1);
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


    @GetMapping(value ="/myPage_delete")
    public String MyPageDelete(){
        return "member/myPageDelete";
    }

    // MyPage
    // 멤버 상태 변경(Put)
    // 로그인세션에서 아이디값을 전달받아 member_delete 페이지로 이동처리.
    @PostMapping(value ="/myPage_delete")
    public String MyPageDelete(String memId) throws MemberNotFoundException {
        Integer memStatus=0;
        String loginId=(String)session.getAttribute("loginId");
        Member member=memberRepository.findByMemId(loginId);
      if(member.getMemId().equals(memId)) {
            member.setMemStatus(memStatus);
            myPageService.removeMe(member);
            return "member/endPage";
        }
        else{
            return "login";
        }
    }
}
