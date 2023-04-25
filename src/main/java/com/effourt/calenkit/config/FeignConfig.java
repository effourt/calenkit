package com.effourt.calenkit.config;

import feign.Logger;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.effourt.calenkit.client")
public class FeignConfig {

    @Bean
    Logger.Level githubFeignClientLoggerLevel() {
        return Logger.Level.FULL;
    }
}
