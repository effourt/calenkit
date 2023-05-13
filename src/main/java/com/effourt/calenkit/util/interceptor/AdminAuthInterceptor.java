package com.effourt.calenkit.util.interceptor;

import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    private MemberRepository memberRepository;

    public AdminAuthInterceptor(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");
        Member findMember = memberRepository.findByMemId(loginId);

        log.info("AdminAuthInterceptor loginId={}", loginId);
        log.info("AdminAuthInterceptor status={}", findMember.getMemStatus());

        if (findMember.getMemStatus() != 9) {
            if (findMember.getMemStatus() == 0) {
                session.invalidate();
                response.sendRedirect(request.getContextPath() + "/login/form");
                return false;
            }
            response.sendRedirect(request.getContextPath() + "/");
            return false;
        }
        return true;
    }
}
