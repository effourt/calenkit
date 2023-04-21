package com.effourt.calenkit.service;

import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.dto.EmailMessage;
import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.util.EmailSend;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
    private final EmailSend emailSend;

    public Member getMemberById(String id) {
        return memberRepository.findByMemId(id);
    }

    /**[email로 로그인 행위] : loginByEmail() */
    // => MemberRepository.find
    // => EmailSend
    // => 세션 값 저장

    // [이메일 로그인] 아이디/비밀번호 존재 여부 확인
    public String checkMember(String memberId) {
        Member member = memberRepository.findByMemId(memberId);
        String loginType = "";
        if (member != null) {
            if (member.getMemPw() != null) {
                //아이디 존재 O, 비밀번호 존재 O
                loginType = "PASSWORD_LOGIN";
            } else {
                //아이디 존재 O, 비밀번호 존재 X
                //로그인 코드 생성 및 메일 전송
                loginType = "CODE_LOGIN";
            }
        } else {
            //아이디 존재 X, 비밀번호 존재 X
            //회원가입 코드 생성 및 메일 전송
            loginType = "JOIN_LOGIN";
        }

        return loginType;
    }

    // 로그인 or 회원가입 코드를 생성하고 세션에 저장
    // 세션키를 쿠키값으로 하는 쿠키를 생성하고 쿠키명을 클라이언트의 메일로 전송
    // 세션에 저장된 ACCESS KEY : 아이디(이메일) + ACCESS
    public void sendCode(String id, EmailMessage emailMessage, HttpSession session) {
        //로그인/회원가입 코드 생성
        emailSend.createAccessCode(id, session);

        //메일 전송
        emailSend.sendMail(emailMessage);
    }

    //[sns로 로그인 행위] : loginBySNS()
    // => MemberRepository.save
    // => AuthRepository.save
    // => AuthRepository.update
    // => 세션 값 저장
}
