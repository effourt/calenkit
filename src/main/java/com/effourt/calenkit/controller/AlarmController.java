package com.effourt.calenkit.controller;

import com.effourt.calenkit.domain.Alarm;
import com.effourt.calenkit.dto.AlarmCate;
import com.effourt.calenkit.repository.AlarmRepository;
import com.effourt.calenkit.repository.ScheduleRepository;
import com.effourt.calenkit.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmRepository alarmRepository;
    private final ScheduleRepository scheduleRepository;
    private final AlarmService alarmService;

    /**
     * 개인알람 리소스를 리스트로 조회
     * @param alMid
     * @return
     */
    // http://localhost:8080/alarms/member
    // http://localhost:8080/alarms/Test1@test1,com
    @GetMapping("/alarms/{alMid}")
    @ResponseBody
    public List<Alarm> showMyAlarmList(@PathVariable String alMid, HttpSession session){
        //String loginId = (String)session.getAttribute("loginId");
        String loginId = "member";
        List<Alarm> alarmList = new ArrayList<>();
        if(loginId.equals(alMid)){
            alarmList = alarmRepository.findByAlMid(loginId);
            return alarmList;
        } else {
            return null;
        }
    }

    /**
     * 일정 수정 일어날 시 알람 리소스 추가 - 1
     * 일정 삭제 일어날 시 알람 리소스 추가 - 0
     * @return
     */
    // http://localhost:8080/alarms/1?scNo=57
    // http://localhost:8080/alarms/0?scNo=58
    @PostMapping ("/alarms/{kind}")
    @ResponseBody
    public String addAlarms(@PathVariable int kind, @RequestParam int scNo) {

        if(AlarmCate.MODIFY_SCHEDULE.ordinal()==kind){
            alarmService.addAlarmByModifySchedule(scNo);
        } else if (AlarmCate.DELETE_SCHDULE.ordinal()==kind){
            alarmService.addAlarmByDeleteSchedule(scNo);
        } else {
            return "fail";
        }
        return "ok";
    }

}