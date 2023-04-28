package com.effourt.calenkit.service;

import com.effourt.calenkit.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Transactional
@SpringBootTest
class MyPageServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Test
    void nameCheck() {
        int cnt=memberRepository.findByMemName("mem");

        System.out.println("cnt :"+cnt);
    }
}