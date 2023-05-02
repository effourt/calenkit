package com.effourt.calenkit.controller;

import com.effourt.calenkit.domain.Alarm;
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
import javax.servlet.http.HttpSession;
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
    public String main(Model model, HttpSession session) {
        //세션에서 로그인아이디 반환받아 저장
        //String loginId = (String)session.getAttribute("loginId");
        String loginId = "employee";

        //개인 알람리스트 조회
        List<Alarm> alarmList = alarmRepository.findByAlMid(loginId);
        List<String> titleList = new ArrayList<>();

        for(int i=0; i<alarmList.size(); i++){
            titleList.add(scheduleRepository.findByScNo(alarmList.get(i).getAlScno()).getScTitle());
        }
        if(alarmList.size()!=0){
            model.addAttribute("alarmList", alarmList);
        }
        model.addAttribute("titleList", titleList);
        for(int i=0;i<alarmList.size(); i++) {
            System.out.println("i = " + i);
            System.out.println("getAlCate = " + alarmList.get(i).getAlCate());
            System.out.println("getAlMid = " + alarmList.get(i).getAlMid());
            System.out.println("getAlScno = " + alarmList.get(i).getAlScno());
            System.out.println("getAlStatus = " + alarmList.get(i).getAlStatus()); //1=출력, 0=휴지통
            System.out.println("titleList = "+titleList.get(i));
            System.out.println("======================================");
        }
        /*//개인 스케줄리스트 조회
        List<Schedule> scheduleList=myScheduleService.getMySchedule(loginId, null);
        model.addAttribute("scheduleList", scheduleList);

        //개인 즐겨찾기리스트 조회
        List<Schedule> bookmarkList=myScheduleService.getBookmark(loginId, null);
        model.addAttribute("bookmarkList", bookmarkList);*/

        return "main";
    }

    /** 권한 있는 일정 전체 출력
     *
     * @return 캘린더 라이브러리에 필요한 필드명 : 일정값 을 매핑한 맵리스트
     */
    @GetMapping("main_ajax")
    @ResponseBody
    public List<Map> mainAJAX() {
        String id="employee"; //session으로 현재 아이디 받아오기
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
            map.put("url", "detail/" + schedule.getScNo());
            mapList.add(map); //map에 일정 저장
        }
        return mapList; //일정이 저장된 mapList값 보내기
    }

    /** 일정 상세페이지 이동
     *
     * @param scNo
     * @return 일정 상세 페이지 HTML
     */
    @RequestMapping("detail/{scNo}")
    public String ScheduleDetail(@PathVariable Integer scNo, Model model) {
        model.addAttribute("schdule",scheduleRepository.findByScNo(scNo));
        return "detail";
    }

    /** 일정 추가
     *
     * @return 일정 상세 페이지 URL
     */
    @GetMapping("add")
    public String addSchedule() {
        String id="employee"; //현재 세션 아이디
        Integer scNo=myScheduleService.addMySchedule(id); //일정 추가

        return "detail/"+scNo; //추가된 일정 상세 페이지로 이동
    }

   @GetMapping("detail/goToRecycleBin/{scNo}")
    public String goToRecycleBin(@PathVariable Integer scNo) {
        myScheduleService.goToRecycleBin(scNo);
        return "/main";
    }

    /*@GetMapping("delete/{scNo}")
    public String deleteSchedule(@PathVariable Integer scNo) {
        String id="employee"; //현재 세션 아이디
        myScheduleService.removeSchedule(scNo, id);

        return "main";
    }*/
}