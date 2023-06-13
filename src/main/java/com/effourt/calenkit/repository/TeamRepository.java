package com.effourt.calenkit.repository;

import com.effourt.calenkit.domain.Team;

import java.util.List;

public interface TeamRepository {

    Integer save(Team team);
    Integer update(Team team);
    Integer delete(Integer teamSno, String teamMid);
    List<Team> findByMid(String teamMid);
    Team findBySnoAndMid(Integer teamSno, String teamMid);
    List<Integer> findByid(String teamMid);
    List<Integer> findByBookmark(String teamMid);
    List<Team> findBySno(Integer teamSno);
}
