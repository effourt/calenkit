package com.effourt.calenkit.service;

public class MyPageService {
    // [내정보 변경 행위] : modifyMe()
    // => MemberRepository.update

    // [비밀번호 변경 행위] : modifyPassword()
    // => MemberRepository.find
    // => EmailSendService
    // => MemberRepository.update

    // [회원탈퇴 행위] : removeMe()
    // => MemberRepository.update

    // [로그아웃 행위] : logout()
    // => 세션 값 삭제
}