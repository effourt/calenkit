package com.effourt.calenkit.config;

import com.effourt.calenkit.repository.AuthRepository;
import com.effourt.calenkit.repository.AuthRepositoryImpl;
import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.repository.MemberRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MybatisConfig {

    private final SqlSessionTemplate sqlSessionTemplate;

    @Bean
    public AuthRepository authRepository() {
        return new AuthRepositoryImpl(sqlSessionTemplate);
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemberRepositoryImpl(sqlSessionTemplate);
    }



}
