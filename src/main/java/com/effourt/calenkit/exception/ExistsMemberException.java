package com.effourt.calenkit.exception;

import java.lang.reflect.Member;

//회원정보 검색 명령이 실행될때 사용자로부터 전달받은 아이디의 회원정보가
//존재할 경우 발생되어 처리하기 위한 예외 클래스
public class ExistsMemberException extends Exception {
    private static final long serialVersionUID = 1L;

    private Member member;

    public ExistsMemberException() {
        // TODO Auto-generated constructor stub
    }

    public ExistsMemberException(String message, Member member) {
        super(message);
        this.member=member;
    }

    public ExistsMemberException(String s) {
    }
}