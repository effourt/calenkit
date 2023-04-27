package com.effourt.calenkit.controller;

import com.effourt.calenkit.domain.Schedule;
import com.effourt.calenkit.domain.Team;
import com.effourt.calenkit.repository.ScheduleRepository;
import com.effourt.calenkit.repository.TeamRepository;
import com.effourt.calenkit.repository.TeamRepositoryImpl;
import com.effourt.calenkit.service.MyScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ScheduleController {
    private final MyScheduleService myScheduleService;
    private final SqlSessionTemplate sqlSessionTemplate;
    private final TeamRepository teamRepository;
    private final ScheduleRepository scheduleRepository;

    /** 달력에 일정 출력(메인페이지)
     *
     */
    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("testTitle", "일정제목");
        /*model.addAttribute("scheduleList", myScheduleService.getMySchedule("member","2023-04-27"));*/

        return "main";
    }
}
