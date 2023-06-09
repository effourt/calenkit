package com.effourt.calenkit.repository;

import com.effourt.calenkit.domain.Team;
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
    public Integer save(Team team) {
        return sqlSessionTemplate.getMapper(TeamMapper.class).save(team);
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
    public Team findBySnoAndMid(Integer teamSno, String teamMid) {
        return sqlSessionTemplate.getMapper(TeamMapper.class).findBySnoAndMid(teamSno,teamMid);
    }

    @Override
    public List<Integer> findByid(String teamMid) {
        return sqlSessionTemplate.getMapper(TeamMapper.class).findByid(teamMid);
    }

    @Override
    public List<Integer> findByBookmark(String teamMid) {
        return sqlSessionTemplate.getMapper(TeamMapper.class).findByBookmark(teamMid);
    }

    @Override
    public List<Team> findBySno(Integer teamSno) {
        return sqlSessionTemplate.getMapper(TeamMapper.class).findBySno(teamSno);
    }
}
