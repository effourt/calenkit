package com.effourt.calenkit.config;

import com.effourt.calenkit.dto.AuthUserInfoResponse;
import feign.Logger;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

public class FeignConfig {

    @Bean
    Logger.Level githubFeignClientLoggerLevel() {
        return Logger.Level.HEADERS;
    }
}
