package com.effourt.calenkit.controller;

import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.domain.Team;
import com.effourt.calenkit.dto.TeamMember;
import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.repository.TeamRepository;
import com.effourt.calenkit.service.TeamScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamController {

    private final TeamScheduleService teamScheduleService;
    private final TeamRepository teamRepository;

    //[GET] : http://localhost:8080/teams/{}
    @GetMapping
    public String showTeamPopup(){
        return "teamPopup";
    }

    //[POST] : http://localhost:8080/teams
    @PostMapping
    public String searchTeam(){
        //teamRepository.findBySno(scNo);
        return "teamPopup";
    }

    //
}
