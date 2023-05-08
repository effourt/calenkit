package com.effourt.calenkit.util.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");
        log.info("LoginInterceptor loginId={}", loginId);
        if (loginId == null) {
            String requestURI = request.getRequestURI();
            String queryString = request.getQueryString();
            log.info("requestURI={}", requestURI);
            log.info("queryString={}", queryString);
            if(queryString==null || queryString.equals("")) session.setAttribute("returnURI",requestURI);
            else session.setAttribute("returnURI",requestURI+"?"+queryString);
            response.sendRedirect(request.getContextPath() + "/login/form");
            return false;
        }
        return true;
    }
}
