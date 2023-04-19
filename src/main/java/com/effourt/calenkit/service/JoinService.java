package com.effourt.calenkit.service;

public class JoinService {
    //[email로 회원가입 행위] : joinByEmail()
    // => EmailSend
    // => MemberRepository.save

    //[sns로 회원가입 행위] : joinBySNS()
    // => MemberRepository.save
    // => AuthRepository.save
}
