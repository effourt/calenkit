package com.effourt.calenkit.controller;

import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.dto.EmailMessage;
import com.effourt.calenkit.service.JoinService;
import com.effourt.calenkit.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

//@Controller
@RequiredArgsConstructor
public class MemberController {

    private final LoginService loginService;
    private final JoinService joinService;

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
    @PostMapping("")
    public String loginBySocial(String id, HttpSession session) {
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
}
