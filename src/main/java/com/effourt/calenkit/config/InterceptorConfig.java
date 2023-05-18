package com.effourt.calenkit.config;

import com.effourt.calenkit.util.interceptor.AdminAuthInterceptor;
import com.effourt.calenkit.util.interceptor.LoginInterceptor;
import com.effourt.calenkit.util.interceptor.UserAuthInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final UserAuthInterceptor userAuthInterceptor;
    private final AdminAuthInterceptor adminAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //유저 로그인 상태 체크 인터셉터
        registry.addInterceptor(loginInterceptor)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/js/**", "/img/**", "/library/**", "*.ico", "/login/**", "/member/logout", "/join/**", "/error");
        //유저 권한 체크 인터셉터
        registry.addInterceptor(userAuthInterceptor)
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/js/**", "/img/**", "/library/**", "*.ico", "/login/**", "/member/logout", "/join/**", "/error", "/members/admin/**");
        //관리자 권한 체크 인터셉터
        registry.addInterceptor(adminAuthInterceptor)
                .order(3)
                .addPathPatterns("/members/admin/**")
                .excludePathPatterns("/css/**", "/js/**", "/img/**", "/library/**", "*.ico", "/login/**", "/logout", "/join/**", "/error");
    }
}
