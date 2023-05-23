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

    @Transactional
    public void modifyMe(Member member) {

        memberRepository.update(member);
    }

    @Transactional
    public void modifyPassword(Member loginMember,String password1){
        loginMember.setMemPw(passwordEncoder.encode(password1));
        memberRepository.updatePassword(loginMember);
    }

    @Transactional
    public void removeMe(Member member) {
        memberRepository.updateStatus(member);
    }

}