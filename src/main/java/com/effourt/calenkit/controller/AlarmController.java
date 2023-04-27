package com.effourt.calenkit.controller;

import com.effourt.calenkit.domain.Alarm;
import com.effourt.calenkit.repository.AlarmRepository;
import com.effourt.calenkit.repository.ScheduleRepository;
import com.effourt.calenkit.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import java.util.List;

// return :  redirect 이동 혹은 객체로 응답할 예정
@Controller
@RequiredArgsConstructor
@RequestMapping("/alarms")
public class AlarmController {

    private final AlarmRepository alarmRepository;
    private final ScheduleRepository scheduleRepository;
    private final AlarmService alarmService;
    //private final MyScheduleService myScheduleService;

    //개인알람리스트 조회
    //권한처리(로그인한 회원만 요청 가능)
    //[GET] : http://localhost:8080/alarms
    @GetMapping
    public String showMyAlarmList(Model model){
        String loginId = "member";
        List<Alarm> alarmList = alarmRepository.findByAlMid(loginId);
        model.addAttribute("alarmList", alarmList);
        return "test";
    }
    /*
    @GetMapping("")
    public String showAlarmList(HttpSession session, Model model){
        String loginId = (String)session.getAttribute("loginId");
        List<Alarm> alarmList = alarmRepository.findByAlMid(loginId);
        model.addAttribute("alarmList", alarmList);
        return "test";
    }
    */

    /*
    //리다이렉트해서 여기서 요청 처리될 수 있도록 만들 것임 (일정 삭제 or 일정 수정)
    //권한처리(로그인한 회원만 요청 가능)
    @RequestMapping ("/add/")
    public String AddAlarmBySchedule(Integer scNo){
        if(scheduleRepository.findByScNo(scNo).getScStatus()==0){ //일정 삭제 시
            alarmService.addAlarmDeleteSchedule(scNo);
        } else { //일정 수정 시
            alarmService.addAlarmByModifySchedule(scNo);
        }
        return "test";
    }
    */

    //1. 일정 수정 >> 비동기로 스케줄테이블 수정 처리하는 요청 처리 메소드 PATCH
    //2. 일정 수정 >> 동행에게 알람을 울리기 위한 요청 처리 메소드

}
