package com.effourt.calenkit.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice("com.effourt.calenkit.controller")
public class EmailExceptionHandler {

    @ExceptionHandler(AddressException.class)
    public String addressEx(Exception e, HttpServletRequest request) {
        log.error("[EmailSendingError] Exception occurred at {}, ExceptionMessage={}, ", request.getRequestURI(), e.getMessage());
        return "이메일 전송에 실패하였습니다.";
    }

    @ExceptionHandler(MailSendException.class)
    public String mailSendEx(Exception e, HttpServletRequest request) {
        log.error("[EmailSendingError] Exception occurred at {}, ExceptionMessage={}, ", request.getRequestURI(), e.getMessage());
        return "유효하지 않은 이메일입니다.";
    }
}
