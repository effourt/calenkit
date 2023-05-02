package com.effourt.calenkit.config;

import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.service.LoginService;
import com.effourt.calenkit.util.interceptor.LoginInterceptor;
import com.effourt.calenkit.util.interceptor.UserAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private final MemberRepository memberRepository;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //유저 로그인 상태 체크 인터셉터
        registry.addInterceptor(new LoginInterceptor())
                .order(1)
                .addPathPatterns("")
                .excludePathPatterns("");
        //유저 권한 체크 인터셉터
        registry.addInterceptor(new UserAuthInterceptor(memberRepository))
                .order(2)
                .addPathPatterns("");
    }
}
