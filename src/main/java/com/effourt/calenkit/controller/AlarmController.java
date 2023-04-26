package com.effourt.calenkit.controller;

import com.effourt.calenkit.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// return :  redirect 이동 혹은 객체로 응답할 예정
@Controller
@RequiredArgsConstructor
@RequestMapping("/Alarms")
public class AlarmController {

    private final AlarmRepository alarmRepository;

    //알람 조회
    //[GET] : http://localhost:8080/alarms
//    @GetMapping
    public String a(){
        //
        return "teamPopup";
    }

    //알람
//    @PostMapping
    //[POST] : http://localhost:8080/alarms
    public String b(){
        return "teamPopup";
    }
//    @GetMapping
    public String c(){
        return "teamPopup";
    }

}
