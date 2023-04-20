package com.effourt.calenkit.repository;

import com.effourt.calenkit.domain.Auth;
import com.effourt.calenkit.domain.Team;
import com.effourt.calenkit.dto.TeamMember;
import com.effourt.calenkit.mapper.ScheduleMapper;
import com.effourt.calenkit.mapper.TeamMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TeamRepositoryImpl implements TeamRepository {

    private final SqlSessionTemplate sqlSessionTemplate;

    @Override
    public Team save(Team team) {
        sqlSessionTemplate.getMapper(TeamMapper.class).save(team);
        return team;
    }

    @Override
    public Integer update(Team team) {
        return sqlSessionTemplate.getMapper(TeamMapper.class).update(team);
    }

    @Override
    public Integer delete(Integer teamSno, String teamMid) {
        return sqlSessionTemplate.getMapper(TeamMapper.class).delete(teamSno,teamMid);
    }

    @Override
    public List<Team> findByMid(String teamMid) {
        return sqlSessionTemplate.getMapper(TeamMapper.class).findByMid(teamMid);
    }

    @Override
    public List<TeamMember> findBySno(String teamSno) {
        return sqlSessionTemplate.getMapper(TeamMapper.class).findBySno(teamSno);
    }
}
