package com.effourt.calenkit.controller;

import com.effourt.calenkit.domain.Auth;
import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.dto.AuthUserInfoResponse;
import com.effourt.calenkit.dto.EmailMessage;
import com.effourt.calenkit.dto.AccessTokenRequest;
import com.effourt.calenkit.dto.AccessTokenResponse;
import com.effourt.calenkit.exception.MemberNotFoundException;
import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.service.AdminService;
import com.effourt.calenkit.service.JoinService;
import com.effourt.calenkit.service.LoginService;
import com.effourt.calenkit.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final LoginService loginService;
    private final JoinService joinService;
    private final MyPageService myPageService;
    private final AdminService adminService;
    private final MemberRepository memberRepository;

    //DB에서 아이디 체크
    //아이디 존재 O, 비밀번호 O : PASSWORD_LOGIN
    //아이디 존재 O, 비밀번호 X : CODE_LOGIN
    //아이디 존재 X, 비밀번호 X : JOIN_LOGIN
//    @PostMapping("")
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
    @GetMapping("login")
    public String login() {
        return "login";
    }

    //패스워드로 로그인
//    @PostMapping("")
    public String loginByPassword(@ModelAttribute Member member, HttpSession session) {
        Member findMember = loginService.getMemberById(member.getMemId());
        if (findMember.getMemPw().equals(member.getMemPw())) {
            session.setAttribute("loginId", member.getMemId());
        }
        return "";
    }

    //로그인 코드로 로그인
//    @PostMapping("")
    public String loginByCode(String id, String code, HttpSession session) {
        String loginCode = (String) session.getAttribute(code);
        if (loginCode.equals(id + "ACCESS")) {
            session.setAttribute("loginId", id);
        }
        return "";
    }


    //소셜 로그인
    @GetMapping("/login/kakao")
    public String loginByKakao(@RequestParam String code, HttpSession session) {
        log.info("code={}", code);
        AccessTokenRequest accessTokenRequest = AccessTokenRequest.builder()
                .clientId("ce578a556d58e95706684b75a588e1b5")
                .grantType("authorization_code")
                .redirectUri("http://localhost:8080/login/kakao")
                .code(code)
                .build();
        //Access 토큰 발급
        AccessTokenResponse accessToken = loginService.getAccessToken(accessTokenRequest);
        //Access 토큰으로 카카오 리소스 서버에서 사용자 정보 가져오기
        AuthUserInfoResponse userInfo = loginService.getAuthUserInfo(accessToken.getAccessToken());
        log.info("userEmail={}", userInfo.getEmail());

        //사용자 이메일이 DB에 존재하지 않으면 Access 토큰, Refresh 토큰 저장 후 회원가입 실시
        //사용자 이메일이 DB에 존재하지만 Access 토큰, Refresh 토큰이 설정되어 있지 않으면 저장 후 로그인
        //사용자 이메일이 DB에 존재하고 Access 토큰, Refresh 토큰이 존재하는 경우 토큰 UPDATE
        Member member = loginService.getMemberById(userInfo.getEmail());
        if (member == null) {
            //사용자 이메일이 DB에 존재하지 않는 경우
            joinService.joinBySns(userInfo, accessToken);
        } else if (member.getMemAuthId() == 0) {
            //사용자 이메일이 DB에 존재하지만 Access 토큰, Refresh 토큰이 설정되어 있지 않는 경우
            Auth auth = loginService.saveToken(accessToken);
            member.setMemAuthId(auth.getAuthId());
            loginService.saveMember(member);
        } else if (member.getMemAuthId() != 0) {
            //사용자 이메일이 DB에 존재하고 Access 토큰, Refresh 토큰이 존재하는 경우
            loginService.updateToken(member.getMemId(), member.getMemAuthId());
        }

        session.setAttribute("loginId", userInfo.getEmail());
        return "main";
    }

    //회원가입 후 이메일 로그인
//    @PostMapping("")
    public String loginByJoin(String id, String code, HttpSession session, Model model) {
        String joinCode = (String) session.getAttribute(code);
        if (joinCode.equals(id + "ACCESS")) {
            model.addAttribute("id", id);
        }
        return "";
    }

    //    @GetMapping("")
    public String logout(HttpSession session) {
        session.invalidate();
        return "";
    }

    /** 회원가입 */
    // 이메일 회원가입
    // 입력 데이터 : 아이디(이메일), 비밀번호, 닉네임, 프로필 이미지(선택)
//    @PostMapping("")
    public String join(@ModelAttribute Member member) {
        joinService.joinByEmail(member);
        return "";
    }

    /** 관리자 */
    // Admin
    @GetMapping(value = "/admin")
    public String AdminList() {
        return "admin";
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
    // MyPage
    // 멤버 닉네임 검색 후 중복 확인(GET)
    // Ajax 처리를 위해 닉네임 값 반환
    @GetMapping(value = "/myPage_name")
    @ResponseBody
    public String MyPageName(@RequestParam String memName) {
        Member member=memberRepository.findByMemName(memName);
        return member.getMemName();
    }

    // MyPage
    // 멤버 정보변경(GET)
    // 로그인세션에서 아이디값을 전달받아 member_modify 페이지로 이동
    @GetMapping(value ="/myPage_modify")
    public String MyPageModify(Model model, HttpSession session) throws MemberNotFoundException {
        Member member=(Member)session.getAttribute("loginMember");
        model.addAttribute("member", memberRepository.findByMemId(member.getMemId()));
        return "myPage";
    }


    // MyPage
    // 멤버 정보변경(PATCH)
    // Form태그를 통해 전달받은 값들을 member 객체에 update 후 member_modify 페이지로 이동
    @PatchMapping(value ="/myPage_modify")
    public String MyPageModify(HttpSession session) throws MemberNotFoundException {
        Member member=(Member)session.getAttribute("loginMember");
        myPageService.modifyMe(member);

        return "redirect:myPage";
    }


    // MyPage
    // 멤버 비밀번호 정보변경(Put)
    // 로그인세션에서 아이디값을 전달받아 member_pwModify 페이지로 이동처리.
    @PutMapping(value ="/myPage_pwModify")
    public String MyPagePwModify(HttpSession session) throws MemberNotFoundException {
        Member member=(Member)session.getAttribute("loginMember");
        myPageService.modifyPassword(member);
        return "myPage";
    }
    // MyPage
    // 멤버 상태 변경(Put)
    // 로그인세션에서 아이디값을 전달받아 member_delete 페이지로 이동처리.
    @PutMapping(value ="/myPage_delete")
    public String MyPageDelete(HttpSession session) throws MemberNotFoundException {
        Member member=(Member)session.getAttribute("loginMember");
        myPageService.removeMe(member);
        return "myPage";
    }

}
