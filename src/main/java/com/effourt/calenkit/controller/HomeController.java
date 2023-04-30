package com.effourt.calenkit.controller;

import com.effourt.calenkit.domain.Alarm;
import com.effourt.calenkit.domain.Team;
import com.effourt.calenkit.repository.AlarmRepository;
import com.effourt.calenkit.repository.ScheduleRepository;
import com.effourt.calenkit.repository.TeamRepository;
import com.effourt.calenkit.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final AlarmRepository alarmRepository;
    private final TeamRepository teamRepository;
    //private final MyScheduleService myScheduleService;


    // => 권한처리 (로그인한 회원만 요청 가능한 요청처리메소드)
    // http://localhost:8080/hihi
    @GetMapping("/hihi")
    public String main( HttpSession session, Model model){

        model.addAttribute("testTitle", "일정제목");

        //세션에서 로그인아이디 반환받아 저장
        //String loginId = (String)session.getAttribute("loginId");
        String loginId = "member";

        //개인 알람리스트 조회
        List<Alarm> alarmList = alarmRepository.findByAlMid(loginId);
        if(alarmList.size()!=0){
            model.addAttribute("alarmList", alarmList);
        }

        //개인 스케줄리스트 조회
        List<Team> teamList = teamRepository.findByMid(loginId);
        if(teamList.size()!=0){
            model.addAttribute("teamList", teamList);
        }

        //개인 북마크한 스케줄리스트 조회
        List<Team> bookmarkList = new ArrayList<>();
        if(teamList.size()!=0){
            for (Team team : teamList) {
                if (team.getTeamBookmark()==1) {
                    bookmarkList.add(team);
                }
            }
            model.addAttribute("bookmarkList", bookmarkList);
        }
        return "main";
    }



}
