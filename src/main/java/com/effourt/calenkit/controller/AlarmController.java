package com.effourt.calenkit.controller;

import com.effourt.calenkit.domain.Alarm;
import com.effourt.calenkit.dto.AlarmCate;
import com.effourt.calenkit.repository.AlarmRepository;
import com.effourt.calenkit.service.AlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmRepository alarmRepository;
    private final HttpSession httpSession;

    // 클라 -> /app/find-alarm/delete-schedule : DELETE_SCHDULE
    // 클라 -> /app/find-alarm/modify-schedule : MODIFY_SCHEDULE
    // 클라 -> /app/find-alarm/save-team : SAVE_TEAM
    // 클라 -> /app/find-alarm/remove-team : REMOVE_TEAM
    // 클라 -> /app/find-alarm/update-teamlevel-read : UPDATE_TEAMLEVEL_READ
    // 클라 -> /app/find-alarm/update-teamlevel-write : UPDATE_TEAMLEVEL_WRITE
    // 서버 -> /alarm/results

    @MessageMapping("/find-alarm/delete-schedule")
    @SendTo("/alarm/results")
    public void a( ) {
    }

    @MessageMapping("/find-alarm/modify-schedule")
    @SendTo("/alarm/results")
    public void b( ) {
    }
    @MessageMapping("/find-alarm/save-team")
    @SendTo("/alarm/results")
    public Alarm c(String alNo) throws IllegalStateException{
        log.info("1");
        Alarm findAlarm = new Alarm();
        log.info("2");
        String loginId = (String)httpSession.getAttribute("loginId");
        log.info("3");
        if(findAlarm.getAlMid().equals(loginId)){
            log.info("4");
            findAlarm = alarmRepository.findByAlNo(Integer.parseInt(alNo));
            log.info("5");
            return findAlarm;
        }
        log.info("6");
        return findAlarm;
    }

    @MessageMapping("/find-alarm/remove-team")
    @SendTo("/alarm/results")
    public void d( ) {
    }

    @MessageMapping("/find-alarm/update-teamlevel-read")
    @SendTo("/alarm/results")
    public void e( ) {
    }

    @MessageMapping("/find-alarm/update-teamlevel-write")
    @SendTo("/alarm/results")
    public void f( ) {
    }




}
