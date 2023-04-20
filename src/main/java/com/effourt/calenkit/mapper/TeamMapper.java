package com.effourt.calenkit.mapper;

import com.effourt.calenkit.domain.Team;
import com.effourt.calenkit.dto.TeamMember;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeamMapper {

    Integer save(Team team);
    Integer update(Team team);
    Integer delete(Integer teamSno, String teamMid);

    List<Team> findByMid(String teamMid);

    List<TeamMember> findBySno(Integer teamSno);
}
