package com.effourt.calenkit;

import org.springframework.stereotype.Controller;

@Controller
public class TestController {
    //[일정 공유 및 권한 행위]
    // => MemberRepository.find
    // => TeamRepository.find
    // => ScheduleRepository.find
    // [일정 초대 행위]
    // => MemberRepository.find
    // => TeamRepository.save
    // => EmailSend
    // [일정 공유 권한 변경 행위]
    // => MemberRepository.find
    // => TeamRepository.find
    // => TeamRepository.modify
}
