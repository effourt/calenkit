package com.effourt.calenkit.controller;

import com.effourt.calenkit.domain.Alarm;
import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.domain.Schedule;
import com.effourt.calenkit.dto.TeamShare;
import com.effourt.calenkit.exception.ScheduleNotFoundException;
import com.effourt.calenkit.exception.TeamNotFoundException;
import com.effourt.calenkit.repository.*;
import com.effourt.calenkit.service.AlarmService;
import com.effourt.calenkit.service.MyScheduleService;
import com.effourt.calenkit.service.TeamScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ScheduleController {
    private final MyScheduleService myScheduleService;
    private final TeamScheduleService teamScheduleService;
    private final TeamRepository teamRepository;
    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;
    private final AlarmRepository alarmRepository;
    private final AlarmService alarmService;
    private final HttpSession session;

    //http://localhost:8080/
    //http://localhost:8080/main
    /**
     * 달력에 일정 출력(메인페이지)
     */
    @RequestMapping(value={"/","/main"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String main(Model model, HttpServletRequest request) {

        //세션에서 로그인아이디 반환받아 저장
        String loginId = (String)session.getAttribute("loginId");
        Member loginMember  = memberRepository.findByMemId(loginId);

        //개인 조회 (로그인 멤버)
        model.addAttribute("loginMember", loginMember);

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


        //개인 즐겨찾기리스트 조회
        List<Schedule> bookmarkList=myScheduleService.getBookmark(loginId, null);
        model.addAttribute("bookmarkList", bookmarkList);


        //일정 리스트 출력(스크롤) - 첫 페이지 고정 출력
        List<Integer> scNoList=teamRepository.findByid(loginId);
        Integer pageNum=1;

        List<Schedule> scheduleList=myScheduleService.getMySchedule(loginId, null, 0, 10);
        model.addAttribute("scheduleList", scheduleList);

        Integer totalRow=scheduleRepository.countFindAllByScNo(scNoList); //일정 총 갯수
        Integer totalPageCount=(int)Math.ceil(totalRow/(double)10); //전체 페이지 갯수
        model.addAttribute("totalPageCount", totalPageCount);

        return "main";
    }

    /** 권한 있는 일정 전체 출력
     *
     * @return 캘린더 라이브러리에 필요한 필드명 : 일정값 을 매핑한 맵리스트
     */
    @GetMapping("/main_ajax")
    @ResponseBody
    public List<Map> mainAJAX() {
        String loginId = (String)session.getAttribute("loginId"); //session으로 현재 아이디 받아오기
        Date temp=new Date(); //출력 기준 월 받아오기
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM");
        String date=simpleDateFormat.format(temp).toString();

        List<Schedule> scheduleList = myScheduleService.getMySchedule(loginId,null, null, null); //일정 리스트 저장

        List<Map> mapList=new ArrayList<>();

        for(Schedule schedule:scheduleList) { //일정 리스트에서 일정 뽑아내기
            if(schedule.getScStatus()!=0) {
                Map<String, String> map=new HashMap<>(); //일정 저장할 map
                map.put("title", schedule.getScTitle());
                map.put("start", schedule.getScSdate());
                map.put("end", schedule.getScEdate());
                map.put("url", "schedules?scNo=" + schedule.getScNo());
                mapList.add(map); //map에 일정 저장
            }
        }
        return mapList; //일정이 저장된 mapList값 보내기
    }

    /** 일정 상세페이지 이동
     *
     * @param scNo
     * @return 일정 상세 페이지 HTML
     */
    //http://localhost:8080/schedules?scNo=1
    @GetMapping("/schedules")
    public String getMyTeam(@RequestParam int scNo, Model model) throws TeamNotFoundException, ScheduleNotFoundException {
        String loginId = (String)session.getAttribute("loginId");
        List<TeamShare> teamShareList = teamScheduleService.getTeam(scNo);

        for(TeamShare teamShrare:teamShareList){
            if(teamShrare.getTeamMid().equals(loginId)){
                model.addAttribute("loginTeam",teamShrare);//현재 로그인한 team + image
            }
        }
        Schedule schedule = scheduleRepository.findByScNo(scNo); //일정 데이터

        model.addAttribute("schedule",schedule);
        model.addAttribute("teamShareList",teamShareList); //team + image
        log.debug("teamShareList = {}", teamShareList.get(0).getTeamLevel());

        return "detail";
    }

    /** 일정 추가
     *
     * @return 일정 상세 페이지로 redirect
     */
    @GetMapping("/add")
    public String addSchedule(@RequestParam String date) {
        String loginId = (String)session.getAttribute("loginId"); //session으로 현재 아이디 받아오기
        Integer scNo=myScheduleService.addMySchedule(loginId, date); //일정 추가

        return "redirect:/schedules?scNo="+scNo; //추가된 일정 상세 페이지로 이동
    }

    /** 일정 휴지통 이동
     *
     * @param scNo
     * @return 메인페이지로 redirect
     */
    @GetMapping("/goToRecycleBin")
    public String goToRecycleBin(@RequestParam Integer scNo) {
        myScheduleService.goToRecycleBin(scNo); //일정 휴지통 이동
        alarmService.addAlarmByDeleteSchedule(scNo); //관련 알람 미출력, 일정 삭제 알람 추가
        return "redirect:/";
    }

    /** 일정 완전 삭제
     *
     * @param scNo
     * @return 메인페이지로 redirect
     */
    @GetMapping("/delete")
    public String deleteSchedule(@RequestParam Integer scNo) {
        String loginId = (String)session.getAttribute("loginId"); //session으로 현재 아이디 받아오기
        alarmService.removeAlarmByScno(scNo);
        myScheduleService.removeSchedule(scNo, loginId);

        return "redirect:/";
    }

    /** 휴지통에서 일정 복원
     *
     * @param scNo
     * @return 메인페이지로 redirect
     */
    @GetMapping("/restore")
    public String restoreSchedule(@RequestParam Integer scNo) {
        String loginId = (String)session.getAttribute("loginId"); //session으로 현재 아이디 받아오기
        alarmService.restoreAlarm(scNo);
        myScheduleService.restoreSchedule(scNo);

        return "redirect:/";
    }

    /** 즐겨찾기 추가/삭제
     *
     * @param scNo
     */
    @GetMapping("/bookmark")
    public String bookmarkSchedule(@RequestParam Integer scNo) {
        String loginId = (String)session.getAttribute("loginId"); //session으로 현재 아이디 받아오기
        myScheduleService.updateBookmark(scNo, loginId);

        return "redirect:/schedules?scNo="+scNo;
    }

    /** 일정 스크롤 두번째 페이지부터
     *
     * @param model
     * @param currentPage
     * @return
     */
    @ResponseBody
    @GetMapping("/schedule_scroll")
    public Map<String, Object> scheduleScroll(Model model, String currentPage) {
        String loginId = (String)session.getAttribute("loginId"); //session으로 현재 아이디 받아오기
        Map<String, Object> map=new HashMap<>();
        List<Integer> scNoList=teamRepository.findByid(loginId);
        Integer pageNum=null;
        if(currentPage!=null){
            pageNum=Integer.parseInt(currentPage);
        } else if(currentPage==null) {
            pageNum=1;
        }

        //startRowNum부터 rowCount만큼 한 페이지에 출력
        Integer rowCount=10; //한 페이지에 표시할 일정 갯수
        Integer startRowNum=0+(pageNum-1)*rowCount;

        List<Schedule> scheduleList=myScheduleService.getMySchedule(loginId, null, startRowNum, rowCount);
        model.addAttribute("scheduleList", scheduleList);
        map.put("scheduleList", scheduleList);

        //일정 총 갯수
        Integer totalRow=scheduleRepository.countFindAllByScNo(scNoList);

        //전체 페이지 갯수
        Integer totalPageCount=(int) Math.ceil(totalRow/(double)rowCount);
        map.put("totalPageCount", totalPageCount);

        return map;
    }

    @ResponseBody
    @PostMapping("/search_schedule")
    public Map<String, Object> searchScroll(@RequestParam(required = false) String keyword,
                                            @RequestParam(required = false) String filter,
                                            Model model, String currentPage) {
        String loginId = (String)session.getAttribute("loginId"); //session으로 현재 아이디 받아오기

        Map<String, Object> map=new HashMap<>();
        List<Integer> scNoList=teamRepository.findByid(loginId);
        Integer pageNum=null;
        if(currentPage!=null){
            pageNum=Integer.parseInt(currentPage);
        } else if(currentPage==null) {
            pageNum=1;
        }

        //startRowNum부터 rowCount만큼 한 페이지에 출력
        Integer rowCount=10; //한 페이지에 표시할 일정 갯수
        Integer startRowNum=0+(pageNum-1)*rowCount;

        List<Schedule>searchList=myScheduleService.searchSchedule(loginId, keyword, filter, startRowNum, rowCount);
        map.put("searchList", searchList);

        //일정 총 갯수
        Integer totalRow=scheduleRepository.countFindAllByScNo(scNoList);

        //전체 페이지 갯수
        Integer totalPageCount=(int) Math.ceil(totalRow/(double)rowCount);
        map.put("totalPageCount", totalPageCount);

        return map;
    }
}