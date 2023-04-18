package com.effourt.calenkit.repository;

import com.effourt.calenkit.domain.Auth;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthRepository {

    Integer save(Auth auth);
    Integer update(Auth auth);
    Integer delete(Integer authId);
    Auth findByAuthId(Integer authId);
}
