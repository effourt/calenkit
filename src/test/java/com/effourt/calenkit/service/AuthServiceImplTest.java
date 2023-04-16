package com.effourt.calenkit.service;

import com.effourt.calenkit.domain.Auth;
import com.effourt.calenkit.repository.AuthRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class AuthServiceImplTest {

    @Autowired
    AuthRepository authRepository;
    @Autowired
    AuthServiceImpl authServiceImpl;

    @TestConfiguration
    static class TestConfig {
    }

    @Test
    void saveToken() {
        Auth auth = new Auth();
        auth.setAuthAccess("AAAA");
        auth.setAuthRefresh("BBBB");

        Auth savedAuth = authServiceImpl.saveToken(auth);
        assertThat(savedAuth.getAuthAccess()).isEqualTo("AAAA");
        assertThat(savedAuth.getAuthRefresh()).isEqualTo("BBBB");
    }
}