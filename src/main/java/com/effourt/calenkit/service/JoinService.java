package com.effourt.calenkit.service;

import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final MemberRepository memberRepository;

    //[email로 회원가입 행위] : joinByEmail()
    // => EmailSend
    // => MemberRepository.save
    // **** 필수 정보 입력 처리 요망 ****
    public void joinByEmail(Member member) {
        memberRepository.save(member);
    }

    //[sns로 회원가입 행위] : joinBySNS()
    // => MemberRepository.save
    // => AuthRepository.save
}
