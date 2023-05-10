package com.effourt.calenkit.service;


import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.exception.MemberNotFoundException;
import com.effourt.calenkit.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    // [내정보 변경 행위] : modifyMe()
    // => MemberRepository.find
    // => MemberRepository.update
    @Transactional
    public void modifyMe(Member member) {

        memberRepository.update(member);
    }


    // [비밀번호 변경 행위] : modifyPassword()
    // => MemberRepository.find
    // => EmailSendService
    // => MemberRepository.update

    @Transactional
    public void modifyPassword(Member loginMember,String password1){
        loginMember.setMemPw(passwordEncoder.encode(password1));
        memberRepository.updatePassword(loginMember);
    }

    // [회원탈퇴 행위] : removeMe()
    // => MemberRepository.update
    public void removeMe(Member member) {
        memberRepository.updateStatus(member);
    }

}