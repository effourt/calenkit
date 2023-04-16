package com.effourt.calenkit.mapper;

import com.effourt.calenkit.domain.Team;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TeamMapper {

    Integer save(Team team);
    Integer update(Team team);
    Integer delete(Integer teamSno, String teamMid);

}
