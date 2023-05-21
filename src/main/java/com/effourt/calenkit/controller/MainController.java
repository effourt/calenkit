package com.effourt.calenkit.controller;

import com.effourt.calenkit.domain.Alarm;
import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.domain.Schedule;
import com.effourt.calenkit.repository.AlarmRepository;
import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.repository.ScheduleRepository;
import com.effourt.calenkit.repository.TeamRepository;
import com.effourt.calenkit.service.MyScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    private final MyScheduleService myScheduleService;
    private final TeamRepository teamRepository;
    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;
    private final AlarmRepository alarmRepository;
    private final HttpSession session;

    //http://localhost:8080/
    //http://localhost:8080/main

    /**
     * 일정 메인 페이지로 이동
     */
    @GetMapping(value = {"/", "/main"})
    public String getMySidebar(Model model) {

        //세션에서 로그인아이디 반환받아 저장
        String loginId = (String) session.getAttribute("loginId");
        Member loginMember = memberRepository.findByMemId(loginId);

        //개인 조회 (로그인 멤버)
        model.addAttribute("loginMember", loginMember);

        //개인 알람리스트 조회
        List<Alarm> alarmList = alarmRepository.findByAlMid(loginId);
        List<String> titleList = new ArrayList<>();
        for (int i = 0; i < alarmList.size(); i++) {
            titleList.add(scheduleRepository.findByScNo(alarmList.get(i).getAlScno()).getScTitle());
        }
        if (alarmList.size() != 0) {
            model.addAttribute("alarmList", alarmList);
        }
        model.addAttribute("titleList", titleList);

        //권한(아이디 기준)을 가진 일정 목록
        List<Integer> scNoList = teamRepository.findByid(loginId);

        //개인 즐겨찾기리스트 출력(스크롤) - 초기 페이지
        List<Schedule> bookmarkList = myScheduleService.getBookmark(loginId, null, 0, 10);
        Integer bookmarkTotalPageCount = (int) Math.ceil(myScheduleService.countBookmark(loginId) / (double) 10);
        model.addAttribute("bookmarkList", bookmarkList);
        model.addAttribute("bookmarkTotalPageCount", bookmarkTotalPageCount);

        //일정 리스트 출력(스크롤) - 초기 페이지
        List<Schedule> scheduleList = myScheduleService.getMySchedule(loginId, null, 0, 10);
        Integer totalPageCount = (int) Math.ceil(scheduleRepository.countFindAllByScNo(scNoList) / (double) 10);
        model.addAttribute("scheduleList", scheduleList);
        model.addAttribute("totalPageCount", totalPageCount);

        //휴지통 리스트 출력(스크롤) - 초기 페이지
        List<Schedule> recyclebinList = myScheduleService.getRecycleBin(loginId, null, null, 0, 10);
        Integer recyclebinTotalPageCount = (int) Math.ceil(myScheduleService.countRecyclebin(loginId, null, null) / (double) 10);
        model.addAttribute("recyclebinList", recyclebinList);
        model.addAttribute("recyclebinTotalPageCount", recyclebinTotalPageCount);

        return "calendar/main";
    }

    /**
     * 일정 메인 페이지 - 권한 있는 일정 전체 출력
     *
     * @return "캘린더 라이브러리에 필요한 필드명": "일정값" 을 매핑한 맵리스트
     */
    @GetMapping("/list")
    @ResponseBody
    public List<Map> getMySchedule() {
        String loginId = (String) session.getAttribute("loginId"); //session으로 현재 아이디 받아오기
        /*String date=null;*/

        List<Schedule> scheduleList = myScheduleService.getMySchedule(loginId, null, null, null); //일정 리스트 저장

        List<Map> mapList = new ArrayList<>();

        for (Schedule schedule : scheduleList) { //일정 리스트에서 일정 뽑아내기
            if (schedule.getScStatus() != 0) {
                Map<String, String> map = new HashMap<>(); //일정 저장할 map
                map.put("title", schedule.getScTitle());
                map.put("start", schedule.getScSdate());
                map.put("end", schedule.getScEdate());
                map.put("url", "schedules?scNo=" + schedule.getScNo());
                /*map.put("defaultDate", date);*/
                mapList.add(map); //map에 일정 저장
            }
        }
        return mapList; //일정이 저장된 mapList값 보내기
    }
}