package com.effourt.calenkit.exception.handler;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@ControllerAdvice("com.effourt.calenkit.client")
public class FeignClientExceptionHandler {

    @ExceptionHandler(FeignException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String feignEx(FeignException e) {
        log.error("FeignException Status={}", e.status());
        e.printStackTrace();
        return "/login/form";
    }

    @ExceptionHandler(FeignException.FeignClientException.class)
    public String feignClientEx(FeignException.FeignClientException e) {
        log.error("FeignClientException Status={}", e.status());
        e.printStackTrace();
        return "/login/form";
    }

    @ExceptionHandler(FeignException.FeignServerException.class)
    public String feignServerEx(FeignException.FeignServerException e) {
        log.error("FeignServerException Status={}", e.status());
        e.printStackTrace();
        return "/login/form";
    }


    @ExceptionHandler(OAuth2AuthenticationException.class)
    public void authenticationEx(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    @ExceptionHandler(OAuth2AuthorizationException.class)
    public void authorizationEx(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
