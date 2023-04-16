package com.effourt.calenkit.service;

import com.effourt.calenkit.domain.Auth;
import com.effourt.calenkit.repository.AuthRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private AuthRepository authRepository;

    public AuthServiceImpl(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public Auth saveToken(Auth auth) {
        log.info("AuthService.saveToken refresh={}", auth.getAuthRefresh());
        log.info("AuthService.saveToken access={}", auth.getAuthAccess());
        Auth savedAuth = authRepository.save(auth);
        log.info("AuthService.authId={}", savedAuth.getAuthId());
        log.info("AuthService.refresh={}", savedAuth.getAuthRefresh());
        log.info("AuthService.access={}", savedAuth.getAuthAccess());
        return savedAuth;
    }
}
