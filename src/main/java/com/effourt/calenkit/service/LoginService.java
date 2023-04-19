package com.effourt.calenkit.service;

public class LoginService {
    // [email로 로그인 행위] : loginByEmail()
    // => MemberRepository.find
    // => EmailSend
    // => 세션 값 저장

    //[sns로 로그인 행위] : loginBySNS()
    // => MemberRepository.save
    // => AuthRepository.save
    // => AuthRepository.update
    // => 세션 값 저장
}
