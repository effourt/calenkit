package com.effourt.calenkit.repository;

import com.effourt.calenkit.domain.Auth;
import com.effourt.calenkit.mapper.AuthMapper;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class AuthRepositoryImpl implements AuthRepository {

    private SqlSessionTemplate sqlSessionTemplate;

    public AuthRepositoryImpl(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    @Override
    public Auth save(Auth auth) {
        log.info("refresh={}", auth.getAuthRefresh());
        log.info("access={}", auth.getAuthAccess());
        return sqlSessionTemplate.getMapper(AuthMapper.class).save(auth);
    }

    @Override
    public Integer update(Auth auth) {
        return sqlSessionTemplate.getMapper(AuthMapper.class).update(auth);
    }

    @Override
    public Integer delete(Integer authId) {
        return sqlSessionTemplate.getMapper(AuthMapper.class).delete(authId);
    }

    @Override
    public Auth findByAuthId(Integer authId) {
        return sqlSessionTemplate.getMapper(AuthMapper.class).findByAuthId(authId);
    }
}
