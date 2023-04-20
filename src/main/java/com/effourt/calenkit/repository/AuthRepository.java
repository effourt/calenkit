package com.effourt.calenkit.repository;

import com.effourt.calenkit.domain.Auth;

public interface AuthRepository {
    Auth save(Auth auth);
    Integer update(Auth auth);
    Integer delete(Integer authId);
    Auth findByAuthId(Integer authId);
}
