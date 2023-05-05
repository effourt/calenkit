package com.effourt.calenkit.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class EncoderTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void passwordEncoder() {
        String password = "asdqwe123!";
        String encodedPw = passwordEncoder.encode(password);
        log.info("encodedPw={}", encodedPw);
        assertThat(passwordEncoder.matches(password, encodedPw)).isEqualTo(true);
    }
}
