package com.effourt.calenkit.mapper;

import com.effourt.calenkit.domain.Team;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TeamMapper {

    Integer save(Team team);
    Integer update(Team team);
    Integer delete(@Param("teamSno") Integer teamSno, @Param("teamMid")String teamMid);
    List<Team> findByMid(String teamMid);
    List<Integer> findByid(String teamMid);
    Team findBySnoAndMid(@Param("teamSno") Integer teamSno,@Param("teamMid") String teamMid);
    List<Integer> findByBookmark(String teamMid);
    List<Team> findBySno(Integer teamSno);
}
