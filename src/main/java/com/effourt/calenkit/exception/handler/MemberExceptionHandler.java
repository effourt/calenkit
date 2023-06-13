package com.effourt.calenkit.exception.handler;

import com.effourt.calenkit.controller.LoginController;
import com.effourt.calenkit.exception.CodeMismatchException;
import com.effourt.calenkit.exception.MemberNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = {LoginController.class})
public class MemberExceptionHandler {

    @ExceptionHandler(MemberNotFoundException.class)
    public String memberNotFoundEx(Exception e) {
        log.info("[MemberNotFoundException] Input ID={}", e.getMessage());
        return "회원 정보가 존재하지 않습니다.";
    }

    @ExceptionHandler(CodeMismatchException.class)
    public String codeMismatchEx(Exception e) {
        log.info("[CodeMismatchException] Input Code={}", e.getMessage());
        return "코드가 올바르지 않습니다.";
    }
}
