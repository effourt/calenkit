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
        String id="member"; //session으로 현재 아이디 받아오기
        Date temp=new Date(); //출력 기준 월 받아오기
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM");
        String date=simpleDateFormat.format(temp).toString();

        List<Schedule> scheduleList = myScheduleService.getMySchedule(id,null); //일정 리스트 저장

        List<Map> mapList=new ArrayList<>();

        for(Schedule schedule:scheduleList) { //일정 리스트에서 일정 뽑아내기
        Map<String, String> map=new HashMap<>(); //일정 저장할 map
            map.put("title", schedule.getScTitle());
            map.put("start", schedule.getScSdate());
            map.put("end", schedule.getScEdate());
            map.put("url", "localhost:8080/main_" + schedule.getScNo());
            mapList.add(map); //map에 일정 저장
        }
        return mapList; //일정이 저장된 mapList값 보내기
    }
}