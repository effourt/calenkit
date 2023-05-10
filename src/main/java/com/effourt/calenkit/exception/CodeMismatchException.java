package com.effourt.calenkit.exception;

/**
 * 로그인/회원가입 코드가 일치하지 않을 때 발생할 예외
 */
public class CodeMismatchException extends RuntimeException {

    public CodeMismatchException() {
    }

    public CodeMismatchException(String message) {
        super(message);
    }
}
