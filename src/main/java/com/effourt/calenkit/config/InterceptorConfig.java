package com.effourt.calenkit.config;

import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.util.interceptor.LoginInterceptor;
import com.effourt.calenkit.util.interceptor.UserAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final UserAuthInterceptor userAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //유저 로그인 상태 체크 인터셉터
        registry.addInterceptor(loginInterceptor)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/js/**", "/img/**", "/library/**", "/login/**", "/join/**");
        //유저 권한 체크 인터셉터
        registry.addInterceptor(userAuthInterceptor)
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/js/**", "/img/**", "/library/**", "/login/**", "/join/**");
    }
}
