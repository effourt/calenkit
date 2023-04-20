package com.effourt.calenkit.exception;

//회원정보에 대한 변경,삭제,검색 명령이 실행될때 사용자로부터 전달받은 아이디의 회원정보가
//없을 경우 발생되어 처리하기 위한 예외 클래스
public class MemberNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public MemberNotFoundException() {
        // TODO Auto-generated constructor stub
    }

    public MemberNotFoundException(String message) {
        super(message);
    }
}