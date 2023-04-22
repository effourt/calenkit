package com.effourt.calenkit.service;

import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.repository.MemberRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Transactional
@SpringBootTest
class LoginServiceTest {

    @Autowired
    private LoginService loginService;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void getMemberById() {
        //given
        Member member = new Member();
        member.setMemId("ID-1");
        member.setMemPw("PASSWORD-1");
        member.setMemName("NICKNAME-1");

        //when
        memberRepository.save(member);
        Member findMember = loginService.getMemberById(member.getMemId());

        //then
        assertThat(findMember.getMemId()).isEqualTo("ID-1");
        assertThat(findMember.getMemPw()).isEqualTo("PASSWORD-1");
        assertThat(findMember.getMemName()).isEqualTo("NICKNAME-1");
    }

    @Test
    void checkMember() {
        Member member1 = new Member();
        member1.setMemId("ID-1");
        member1.setMemPw("PASSWORD-1");
        member1.setMemName("NICKNAME-1");

        Member member2 = new Member();
        member2.setMemId("ID-2");
        member2.setMemName("NICKNAME-2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        //아이디 존재 O, 비밀번호 존재 O
        String result1 = loginService.checkMember(member1.getMemId());
        //아이디 존재 O, 비밀번호 존재 X
        String result2 = loginService.checkMember(member2.getMemId());
        //아이디 존재 X, 비밀번호 존재 X
        String result3 = loginService.checkMember("ID-4");

        assertThat(result1).isEqualTo("PASSWORD_LOGIN");
        assertThat(result2).isEqualTo("CODE_LOGIN");
        assertThat(result3).isEqualTo("JOIN_LOGIN");
    }

    @Test
    void sendCode() {

    }
}