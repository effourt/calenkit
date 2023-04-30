package com.effourt.calenkit.util.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class UserAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");
        log.info("UserAuthInterceptor loginId={}", loginId);
        if (loginId == null) {
            response.sendRedirect(request.getContextPath() + "/login/form");
        }
        return true;
    }
}
