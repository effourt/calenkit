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
import org.springframework.web.bind.annotation.*;

import javax.mail.Session;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ScheduleController {
    private final MyScheduleService myScheduleService;
    private final SqlSessionTemplate sqlSessionTemplate;
    private final TeamRepository teamRepository;
    private final ScheduleRepository scheduleRepository;

    /**
     * 달력에 일정 출력(메인페이지)
     */
    @GetMapping(value={"/","/main"})
    public String main(Model model) {
        return "main";
    }

    @GetMapping("/main_ajax")
    @ResponseBody
    public List<Map> mainAJAX() {
        String id="member";
        Date temp=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM");
        String date=simpleDateFormat.format(temp).toString();

        List<Schedule> scheduleList = myScheduleService.getMySchedule(id,null);

        List<Map> mapList=new ArrayList<>();

        for(Schedule schedule:scheduleList) {
        Map<String, String> map=new HashMap<>();
            map.put("title", schedule.getScTitle());
            map.put("start", schedule.getScSdate());
            map.put("end", schedule.getScEdate());
            map.put("url", "localhost:8080/main_" + schedule.getScNo());
            mapList.add(map);
        }
        return mapList;
    }
}