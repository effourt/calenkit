package com.effourt.calenkit.controller;

import com.effourt.calenkit.domain.Alarm;
import com.effourt.calenkit.repository.AlarmRepository;
import com.effourt.calenkit.repository.ScheduleRepository;
import com.effourt.calenkit.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/alarms")
public class AlarmController {

    private final AlarmRepository alarmRepository;
    private final ScheduleRepository scheduleRepository;
    private final AlarmService alarmService;

    /**
     * 개인알람 리소스를 리스트로 조회
     * @param alMid
     * @return
     */
    // http://localhost:8080/alarms/{alMid}
    @GetMapping("/{alMid}")
    @ResponseBody
    public List<Alarm> showMyAlarmList(@PathVariable String alMid, HttpSession session){
        String loginId = (String)session.getAttribute("loginId");
        //String loginId = "member";
        List<Alarm> alarmList = new ArrayList<>();
        if(loginId.equals(alMid)){
            alarmList = alarmRepository.findByAlMid(loginId);
             return alarmList;
        } else {
            return null;
        }
    }
    /*
    [
        {
            "alNo": 1,
            "alScno": 1,
            "alMid": "member",
            "alStatus": 1,
            "alTime": "2023-04-27",
            "alCate": 1
        },
        {
            "alNo": 111,
            "alScno": 1,
            "alMid": "member",
            "alStatus": 1,
            "alTime": "2023-04-27",
            "alCate": 1
        }
    ]
    */

    /**
     * 일정 수정 일어날 시 알람 리소스 추가
     * @return
     */
    /*
    // http://localhost:8080/alarms?scNo=1
    @GetMapping("/")
    @ResponseBody
    public String addAlarms(@RequestParam int scNo) {

    }
    */
    //1. 일정 수정 >> 비동기로 스케줄테이블 수정 처리하는 요청 처리 메소드 PATCH
    //2. 일정 수정 >> 동행에게 알람을 울리기 위한 요청 처리 메소드

}
