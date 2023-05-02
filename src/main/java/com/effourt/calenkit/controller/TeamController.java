package com.effourt.calenkit.controller;

import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.domain.Team;
import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.repository.TeamRepository;
import com.effourt.calenkit.service.TeamScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TeamController {

    private final TeamScheduleService teamScheduleService;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    //http://localhost:8080/schedules?scNo=1
    @GetMapping("/schedules")
    public String getMyTeam(@RequestParam int scNo, Model model) {
        List<Team> teamList = teamRepository.findBySno(scNo);
        List<String> imageList = new ArrayList<>();
        for(int i=0; i<teamList.size(); i++){
            imageList.add(memberRepository.findByMemId(teamList.get(i).getTeamMid()).getMemImage());
        }
        model.addAttribute("teamList",teamList);
        model.addAttribute("imageList",imageList);
        System.out.println(teamList.get(0).getTeamMid());
        System.out.println(teamList.get(0).getTeamLevel());
        System.out.println(imageList.get(0));
        System.out.println(teamList.get(1).getTeamMid());
        System.out.println(teamList.get(1).getTeamLevel());
        System.out.println(imageList.get(1));
        return "detail";
    }


    /**
     * 동행 검색하기 위해 필요
     * @param memId
     * @return
     */
    // http://localhost:8080/members?memId=member
    // http://localhost:8080/members?memId=employee
    // http://localhost:8080/members?memId=Test3@test3.com
    @GetMapping("/members")
    @ResponseBody
    public Member searchMyTeam(@RequestParam String memId) {
        return memberRepository.findByMemId(memId);
    }


}
