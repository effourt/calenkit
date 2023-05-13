package com.effourt.calenkit.controller;

import com.effourt.calenkit.domain.Auth;
import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.dto.AccessTokenRequest;
import com.effourt.calenkit.dto.AccessTokenResponse;
import com.effourt.calenkit.dto.AuthUserInfoResponse;
import com.effourt.calenkit.dto.EmailMessage;
import com.effourt.calenkit.exception.CodeMismatchException;
import com.effourt.calenkit.exception.MemberNotFoundException;
import com.effourt.calenkit.repository.AuthRepository;
import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.service.JoinService;
import com.effourt.calenkit.service.LoginService;
import com.effourt.calenkit.util.EmailSend;
import com.effourt.calenkit.util.ImageUpload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
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
    private final ImageUpload imageUpload;
    private final PasswordEncoder passwordEncoder;


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
        if (memId == null) {
            return "이메일이 올바르지 않습니다.";
        }
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
        log.info("회원 아이디 = {}", member.getMemId());
        //세션에 저장된 아이디 검색
        Member findMember = loginService.getMemberById(member.getMemId());

        //회원 존재 여부 및 탈퇴 회원 여부 검증
        if (findMember == null || findMember.getMemStatus() == 0) {
            throw new MemberNotFoundException(member.getMemId());
        }

        //전달된 비밀번호와 검색한 비밀번호(인코딩된 비밀번호)를 비교
        if (member.getMemPw() == null || !passwordEncoder.matches(member.getMemPw(), findMember.getMemPw())) {
            return "비밀번호가 올바르지 않습니다.";
        } else {
            session.setAttribute("loginId", member.getMemId());
            loginService.updateLastLogin(findMember.getMemId());
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
        //회원 존재 여부 및 탈퇴 회원 여부 검증
        Member member = loginService.getMemberById(memId);
        if (member == null || member.getMemStatus() == 0) {
            throw new MemberNotFoundException(memId);
        }

        String code = (String) session.getAttribute(loginCodeMap.get("loginCode"));
        //코드 존재 여부 및 일치 여부 검증
        if (code == null || !code.equals(memId + "ACCESS")) {
            throw new CodeMismatchException(code);
        } else {
            session.setAttribute("loginId", memId);
            loginService.updateLastLogin(memId);
        }
        return "OK";
    }

    /**
     * 회원가입 코드로 회원가입 후 이메일 로그인
     * @param registerMap
     * @param session
     * @return
     */
    @PostMapping("/login/register-code")
    @ResponseBody
    public String loginByJoin(@RequestBody Map<String, String> registerMap, HttpSession session) {
        String memId = registerMap.get("id");
        String code = (String) session.getAttribute(registerMap.get("registerCode"));
        //코드 존재 여부 및 일치 여부 검증
        if (code == null || !code.equals(memId + "ACCESS")) {
            throw new CodeMismatchException(code);
        }

        Member member = loginService.getMemberById(memId);
        if (member != null && member.getMemStatus() == 0) {
            member.setMemStatus(1);
            loginService.update(member);
            session.setAttribute("loginId", memId);
            loginService.updateLastLogin(memId);
            return "RE_JOIN";
        }

        return "JOIN";
    }

    /**
     * 비밀번호 초기화 코드로 로그인 - 비밀번호 초기화 후 로그인 처리
     * @param initializeCodeMap
     * @param session
     * @return
     */
    @PostMapping("/login/initialize-code")
    @ResponseBody
    public String loginByInitialize(@RequestBody Map<String, String> initializeCodeMap, HttpSession session) {
        String memId = initializeCodeMap.get("id");
        //회원 존재 여부 및 탈퇴 회원 여부 검증
        Member member = loginService.getMemberById(memId);
        if (member == null || member.getMemStatus() == 0) {
            throw new MemberNotFoundException(memId);
        }

        //초기화 코드 검증 후 로그인
        String code = (String) session.getAttribute(initializeCodeMap.get("initializeCode"));
        if (code == null || !code.equals(memId + "ACCESS")) {
            throw new CodeMismatchException(code);
        } else {
            //비밀번호 초기화 (null로 지정)
            loginService.updatePassword(memId, null);
            session.setAttribute("loginId", memId);
            loginService.updateLastLogin(memId);
        }
        return "OK";
    }

    @PostMapping("/join/form")
    public String joinForm(@RequestParam String memId, Model model) {
        model.addAttribute("memId", memId);
        return "register";
    }

    /**
     * 이메일 회원가입
     * @param member 아이디(이메일), 비밀번호, 닉네임, 프로필 이미지(선택) 정보 저장 객체
     * @return
     */
    @PostMapping(value = "/join", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    public String join(@RequestPart Member member, @RequestPart MultipartFile profileImage, HttpSession session) {
        //비밀번호를 암호화
        member.setMemPw(passwordEncoder.encode(member.getMemPw()));
        try {
            String filename = imageUpload.uploadImage(profileImage);
            member.setMemImage(filename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //아이디(이메일), 프로필 이미지, 닉네임, 비밀번호를 회원 테이블에 저장
        joinService.joinByEmail(member);
        session.setAttribute("loginId", member.getMemId());
        loginService.updateLastLogin(member.getMemId());
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
            //사용자 이메일이 DB에 존재하지 않은 경우 회원가입 후 로그인
            joinService.joinBySns(userInfo, accessToken);
            log.info("카카오 - 회원가입 후 로그인");
        } else {
            if (member.getMemAuthId() == null) {
                //사용자 이메일이 DB에 존재하지만 Access 토큰, Refresh 토큰이 설정되어 있지 않는 경우
                Auth auth = loginService.saveToken(accessToken);
                member.setMemAuthId(auth.getAuthId());
                //탈퇴회원인 경우, 일반 회원으로 권한 변경
                if (member.getMemStatus() == 0) {
                    member.setMemStatus(1);
                }
                loginService.update(member);
                log.info("카카오 - 토큰 저장 후 로그인");
            } else if (member.getMemAuthId() != null) {
                //사용자 이메일이 DB에 존재하고 Access 토큰, Refresh 토큰이 존재하는 경우
                loginService.updateToken(member.getMemAuthId(), accessToken);
                //탈퇴회원인 경우, 일반 회원으로 권한 변경
                if (member.getMemStatus() == 0) {
                    member.setMemStatus(1);
                    loginService.update(member);
                }
                log.info("카카오 - 토큰 갱신 후 로그인");
            }
        }

        session.setAttribute("loginId", userInfo.getEmail());
        loginService.updateLastLogin(userInfo.getEmail());
        return "redirect:/return-uri";
    }

    /**
     * 로그인 후 Redirect 할 경로 지정
     * @param session
     * @return
     */
    @GetMapping("/return-uri")
    public String returnURI(HttpSession session) {
        String returnURI = (String)session.getAttribute("returnURI");
        log.info("returnURI = {}",returnURI);
        if (returnURI == null) {
            return "redirect:/";
        } else if (returnURI != null || !returnURI.equals("")) {
            session.removeAttribute("returnURI");
            return "redirect:"+returnURI;
        }

        return "redirect:/";
    }

    /**
     * 로그아웃
     * @param session
     * @return
     */
    @GetMapping("/member/logout")
    public String logout(HttpSession session) {
        log.info("로그아웃 시작");
        String id = (String) session.getAttribute("loginId");
        Integer authId = loginService.getMemberById(id).getMemAuthId();
        if (authId != null) {
            loginService.expireToken(authRepository.findByAuthId(authId).getAuthAccess());
        }
        session.invalidate();
        log.info("로그아웃 종료");
        return "redirect:/login/form";
    }
}
