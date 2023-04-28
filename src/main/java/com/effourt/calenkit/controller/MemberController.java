package com.effourt.calenkit.controller;

import com.effourt.calenkit.domain.Auth;
import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.dto.AuthUserInfoResponse;
import com.effourt.calenkit.dto.EmailMessage;
import com.effourt.calenkit.dto.AccessTokenRequest;
import com.effourt.calenkit.dto.AccessTokenResponse;
import com.effourt.calenkit.exception.MemberNotFoundException;
import com.effourt.calenkit.repository.AuthRepository;
import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.service.AdminService;
import com.effourt.calenkit.service.JoinService;
import com.effourt.calenkit.service.LoginService;
import com.effourt.calenkit.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final LoginService loginService;
    private final JoinService joinService;

    private final MemberRepository memberRepository;


    private final AuthRepository authRepository;


    //DB에서 아이디 체크
    //아이디 존재 O, 비밀번호 O : PASSWORD_LOGIN
    //아이디 존재 O, 비밀번호 X : CODE_LOGIN
    //아이디 존재 X, 비밀번호 X : JOIN_LOGIN
    @PostMapping("/check")
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
    @GetMapping("/login/pw")
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
    @PostMapping("/login/code")
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
            loginService.updateToken(member.getMemAuthId(), accessToken);
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

    @GetMapping("/logout")

    public String logout(HttpSession session) {
        String id = (String) session.getAttribute("loginId");
        Integer authId = memberRepository.findByMemId(id).getMemAuthId();
        if (authId != null && authId != 0) {
            loginService.expireToken(authRepository.findByAuthId(authId).getAuthAccess());
        }
        session.invalidate();
        return "login";
    }

    /** 회원가입 */
    // 이메일 회원가입
    // 입력 데이터 : 아이디(이메일), 비밀번호, 닉네임, 프로필 이미지(선택)
//    @PostMapping("")
    public String join(@ModelAttribute Member member) {
        joinService.joinByEmail(member);
        return "";
    }


}
