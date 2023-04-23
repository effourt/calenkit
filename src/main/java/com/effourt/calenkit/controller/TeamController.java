package com.effourt.calenkit.controller;

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

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/team")
public class TeamController {

    private final TeamScheduleService teamScheduleService;
    private final TeamRepository teamRepository;

    // 일정 상세에서 공유버튼 클릭 시 호출될 요청처리 메소드
    // 공유 폼 뷰 페이지 요청
    //[GET] http://localhost:8080/team
    @GetMapping
    public String showTeam(@RequestParam int scNo, Model model){
        List<TeamMember> teamMemberList = teamRepository.findBySno(scNo);
        model.addAttribute("team")

        return "teamPopup";
    }

    //
    //[POST] http://localhost:8080/team
    @PostMapping
    public String addTeam(@RequestParam int scNo){
        teamRepository.findBySno(scNo);
        return "teamPopup";
    }
}
