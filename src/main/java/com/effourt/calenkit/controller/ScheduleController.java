package com.effourt.calenkit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ScheduleController {

    @GetMapping("/test")
    public String test() {


        return "test";
    }
}
