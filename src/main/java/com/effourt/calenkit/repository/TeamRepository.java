package com.effourt.calenkit.repository;

import com.effourt.calenkit.domain.Team;
import com.effourt.calenkit.dto.TeamMember;

import java.util.List;

public interface TeamRepository {

    void save(String id, Integer scNo);
    Integer update(Team team);
    Integer delete(Integer teamSno, String teamMid);
    List<Team> findByMid(String teamMid);
    List<Integer> findByid(String teamMid);
    List<Team> findByBookmark(String teamMid);
    List<Team> findBySno(Integer teamSno);
}
