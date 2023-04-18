package com.effourt.calenkit.service;

import com.effourt.calenkit.domain.Auth;
import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.repository.AuthRepository;
import com.effourt.calenkit.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private AuthRepository authRepository;
    private MemberRepository memberRepository;

    public AuthServiceImpl(AuthRepository authRepository, MemberRepository memberRepository) {
        this.authRepository = authRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public Auth saveToken(Auth auth) {
        log.info("AuthService.saveToken refresh={}", auth.getAuthRefresh());
        log.info("AuthService.saveToken access={}", auth.getAuthAccess());
        authRepository.save(auth);
        log.info("AuthService.authId={}", auth.getAuthId());
        log.info("AuthService.refresh={}", auth.getAuthRefresh());
        log.info("AuthService.access={}", auth.getAuthAccess());
        Member member = new Member();
        member.setMemImage("image");
        member.setMemId("member1");
        member.setMemPw("pw1");
        member.setMemName("memberName1");
        member.setMemStatus(1);
        member.setMemAuthId(auth.getAuthId());
        memberRepository.save(member);
        log.info("Member 데이터 삽입, 토큰 번호={}", auth.getAuthId());
        return auth;
    }
}
