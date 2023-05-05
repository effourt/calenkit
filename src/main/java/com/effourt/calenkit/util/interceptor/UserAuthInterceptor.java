package com.effourt.calenkit.util.interceptor;

import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Component
public class UserAuthInterceptor implements HandlerInterceptor {

    private MemberRepository memberRepository;

    public UserAuthInterceptor(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");
        log.info("UserAuthInterceptor loginId={}", loginId);

        Member findMember = memberRepository.findByMemId(loginId);
        log.info("UserAuthInterceptor findMemberId={}, findMemberStatus={}", findMember.getMemId(), findMember.getMemStatus());
        if (findMember.getMemStatus() == 9) {
            response.sendRedirect("/admin");
        }
        return true;
    }
}
