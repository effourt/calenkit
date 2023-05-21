package com.effourt.calenkit.controller;

import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.service.JoinService;
import com.effourt.calenkit.service.LoginService;
import com.effourt.calenkit.util.ImageUpload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/join")
@RequiredArgsConstructor
public class JoinController {

    private final LoginService loginService;
    private final JoinService joinService;
    private final ImageUpload imageUpload;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 페이지로 이동
     * @param memId
     * @param model
     * @return
     */
    @PostMapping("/form")
    public String joinForm(@RequestParam String memId, Model model) {
        model.addAttribute("memId", memId);
        return "login/register";
    }

    /**
     * 이메일 회원가입
     * @param member 아이디(이메일), 비밀번호, 닉네임, 프로필 이미지(선택) 정보 저장 객체
     * @return
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
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
}
