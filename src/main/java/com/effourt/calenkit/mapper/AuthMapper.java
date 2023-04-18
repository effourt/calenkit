package com.effourt.calenkit.mapper;

import com.effourt.calenkit.domain.Auth;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {

    Integer save(Auth auth);
    Integer update(Auth auth);
    Integer delete(Integer authId);
    Auth findByAuthId(Integer authId);

}
