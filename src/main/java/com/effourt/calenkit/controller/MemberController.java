package com.effourt.calenkit.controller;

import com.effourt.calenkit.domain.Auth;
import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.dto.AccessTokenRequest;
import com.effourt.calenkit.dto.AccessTokenResponse;
import com.effourt.calenkit.dto.AuthUserInfoResponse;
import com.effourt.calenkit.dto.EmailMessage;
import com.effourt.calenkit.repository.AuthRepository;
import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.service.JoinService;
import com.effourt.calenkit.service.LoginService;
import com.effourt.calenkit.util.EmailSend;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final LoginService loginService;
    private final JoinService joinService;

    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;

    private final MessageSource ms;
    private final EmailSend emailSend;


    /**
     * 아이디, 비밀번호 존재 여부 체크
     * 아이디 존재 O, 비밀번호 O : PASSWORD_LOGIN
     * 아이디 존재 O, 비밀번호 X : CODE_LOGIN
     * 아이디 존재 X, 비밀번호 X : JOIN_LOGIN
     * @param idMap
     * @return
     */
    @PostMapping("/login/check")
    @ResponseBody
    public String checkId(@RequestBody Map<String, String> idMap) {
        String memId = idMap.get("id");
        String loginType = loginService.checkMember(memId);
        log.info("loginType={}", loginType);

        return loginType;
    }

    @PostMapping("/login/send-code")
    @ResponseBody
    public String sendCode(@RequestBody Map<String, String> idMap, HttpSession session) {
        String memId = idMap.get("id");
        String subject = ms.getMessage("mail.login-code.subject", null, null);
        String message = ms.getMessage(
                "mail.login-code.message",
                new Object[]{emailSend.createAccessCode(memId, session)},
                null);

        EmailMessage emailMessage = EmailMessage.builder()
                .recipient(memId)
                .subject(subject)
                .message(message)
                .build();
        //이메일 전송
        emailSend.sendMail(emailMessage);

        log.info("email id={}", memId);
        log.info("subject={}", subject);
        log.info("message={}", message);
        return "OK";
    }


    /**
     * 로그인창으로 이동
     */
    @GetMapping("/login/form")
    public String login() {
        return "login";
    }

    /**
     * 패스워드로 로그인
     * @param member
     * @param session
     * @return
     */
    @PostMapping("/login/password")
    @ResponseBody
    public String loginByPassword(@RequestBody Member member, HttpSession session) {
        Member findMember = loginService.getMemberById(member.getMemId());
        if (findMember.getMemPw().equals(member.getMemPw())) {
            session.setAttribute("loginId", member.getMemId());
        } else {
            return "아이디 또는 비밀번호를 잘못 입력하셨습니다.";
        }
        return "OK";
    }

    /**
     * 로그인 코드로 로그인
     * @param loginCodeMap
     * @param session
     * @return
     */
    @PostMapping("/login/login-code")
    @ResponseBody
    public String loginByCode(@RequestBody Map<String, String> loginCodeMap, HttpSession session) {
        String memId = loginCodeMap.get("id");
        String code = (String) session.getAttribute(loginCodeMap.get("loginCode"));
        if (code.equals(memId + "ACCESS")) {
            session.setAttribute("loginId", memId);
        } else {
            return "로그인 코드를 잘못 입력하셨습니다.";
        }
        return "OK";
    }

    /**
     * 회원가입 코드로 회원가입 후 이메일 로그인
     * @param memId
     * @param registerCode
     * @param session
     * @param model
     * @return
     */
    @PostMapping("/login/register-code")
    public String loginByJoin(String memId, String registerCode, HttpSession session, Model model) {
        String joinCode = (String) session.getAttribute(registerCode);
        if (joinCode.equals(memId + "ACCESS")) {
            model.addAttribute("memId", memId);
        }
        return "register";
    }

    @PostMapping("/login/initialize-code")
    @ResponseBody
    public String loginByInitialize(@RequestBody Map<String, String> initializeCodeMap, HttpSession session) {
        String memId = initializeCodeMap.get("id");
        //비밀번호 초기화 (null로 지정)
        loginService.updatePassword(memId, null);
        //초기화 코드 검증 후 로그인
        String code = (String) session.getAttribute(initializeCodeMap.get("initializeCode"));
        if (code.equals(memId + "ACCESS")) {
            session.setAttribute("loginId", memId);
        } else {
            return "초기화 코드를 잘못 입력하였습니다.";
        }
        return "OK";
    }

    /**
     * 이메일 회원가입
     * @param member 아이디(이메일), 비밀번호, 닉네임, 프로필 이미지(선택) 정보 저장 객체
     * @return
     */
    @PostMapping("/join")
    @ResponseBody
    public String join(@RequestBody Member member, HttpSession session) {
        joinService.joinByEmail(member);
        session.setAttribute("loginId", member.getMemId());
        return "OK";
    }

    /**
     * 소셜 로그인
     * @param code
     * @param session
     * @return
     */
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
            log.info("카카오 - 회원가입 후 로그인");
        } else if (member.getMemAuthId() == 0) {
            //사용자 이메일이 DB에 존재하지만 Access 토큰, Refresh 토큰이 설정되어 있지 않는 경우
            Auth auth = loginService.saveToken(accessToken);
            member.setMemAuthId(auth.getAuthId());
            loginService.update(member);
            log.info("카카오 - 토큰 저장 후 로그인");
        } else if (member.getMemAuthId() != 0) {
            //사용자 이메일이 DB에 존재하고 Access 토큰, Refresh 토큰이 존재하는 경우
            loginService.updateToken(member.getMemAuthId(), accessToken);
            log.info("카카오 - 토큰 갱신 후 로그인");
        }

        session.setAttribute("loginId", userInfo.getEmail());
        return "main";
    }

    /**
     * 로그아웃
     * @param session
     * @return
     */
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


}
