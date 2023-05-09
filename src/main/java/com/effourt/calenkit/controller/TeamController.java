package com.effourt.calenkit.controller;

import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.domain.Team;
import com.effourt.calenkit.dto.EmailMessage;
import com.effourt.calenkit.dto.TeamShare;
import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.repository.ScheduleRepository;
import com.effourt.calenkit.repository.TeamRepository;
import com.effourt.calenkit.service.AlarmService;
import com.effourt.calenkit.service.TeamScheduleService;
import com.effourt.calenkit.util.EmailSend;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
@Slf4j
@Controller
@RequiredArgsConstructor
public class TeamController {

    private final TeamScheduleService teamScheduleService;
    private final AlarmService alarmService;

    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final ScheduleRepository scheduleRepository;

    private final MessageSource ms;
    private final EmailSend emailSend;

    /**
     * 회원 조회 (동행 검색할 때 필요)
     * @param memId
     * @return
     */
    // http://localhost:8080/members?memId=member
    // http://localhost:8080/members?memId=employee
    // http://localhost:8080/members?memId=Test3@test3.com
    @GetMapping("/members")
    @ResponseBody
    public Member searchMemberForShare(@RequestParam String memId) {
        return memberRepository.findByMemId(memId);
    }

    @GetMapping("/return-uri")
    public String returnURI(HttpSession session) {
        String returnURI= (String)session.getAttribute("returnURI");
        log.info("returnURI = {}",returnURI);
        if(returnURI==null) {
            return "redirect:/";
        } else if(returnURI.contains("/teams/share/confirm") && (!returnURI.equals("") || returnURI!=null)) {
            return "redirect:"+returnURI;
        }
        return "redirect:/";
    }

    /**
     * 동행 조회 - rest API
     * */
    // http://localhost:8080/teams/share/1
    @GetMapping ("/teams/share/{scNo}")
    @ResponseBody
    public List<TeamShare> searchMyTeam(@PathVariable int scNo) {
        return teamScheduleService.getTeam(scNo);
    }

    /**
     * 동행 추가 (동행추가 + 알람서비스)
     * */
    // http://localhost:8080/teams/share/1  : memId=member
    // http://localhost:8080/teams/share/1  : memId=test@test.com
    // http://localhost:8080/teams/share/1  : memId=employee
    @PostMapping ("/teams/share/{scNo}")
    @ResponseBody
    public String shareTeam(@PathVariable int scNo, @RequestBody Map<String,Object> map, HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");
        String memId = (String)map.get("memId");
        log.info("[shareTeam] scNo = {}",scNo);
        log.info("[shareTeam] memId = {}",memId);
        log.info("[shareTeam] loginId = {}",loginId);
        if(loginId.equals(memId)){
            teamScheduleService.addTeam(scNo,memId);
            alarmService.addAlarmBySaveTeam(scNo,memId); //알람서비스
            log.info("[shareTeam] ok");
            return "ok";
        } else{
            log.info("[shareTeam] fail");
            return "fail";
        }
    }

    /**
     * 동행 추가 (동행추가 + 알람서비스)
     * */
    // http://localhost:8080/teams/share/1  : memId=member
    // http://localhost:8080/teams/share/1  : memId=test@test.com
    // http://localhost:8080/teams/share/1  : memId=employee
    //@PostMapping ("/teams/share/{scNo}")
    public String shareTeam2(@PathVariable int scNo, @RequestParam String memId, HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");
        if(loginId.equals(memId)){
            teamScheduleService.addTeam(scNo,memId);
            log.info("[shareTeam2] teamScheduleService.addTeam");
            alarmService.addAlarmBySaveTeam(scNo,memId); //알람서비스
            log.info("[shareTeam2] alarmService.addAlarmBySaveTeam");
        }
        return "redirect:/schedules?scNo="+scNo;
    }



    /***
     * 동행의 권한 상태 변경 (권한상태변경 + 알람서비스)
     * @param map -> teamLevel은 무조건 - 읽기권한:0, 수정권한:1
     * @return
     */
    // http://localhost:8080/teams/share/1  : teamMid=member?teamLevel=0
    // http://localhost:8080/teams/share/1  : teamMid=member?teamLevel=1
    @PatchMapping("/teams/share/{scNo}")
    @ResponseBody
    public String updateTeamLevel(@PathVariable int scNo,@RequestBody Map<String,Object> map) {
        String id = (String)map.get("teamMid");
        int level = Integer.parseInt(String.valueOf(map.get("teamLevel"))); //String으로 변환한 후 Integer.parseInt
        teamScheduleService.modifyTeamLevel(scNo,id,level);
        if(level==0){ //읽기
            alarmService.addAlarmByUpdateTeamLevelRead(id,scNo);//알람서비스
        } else if(level==1){ //수정
            alarmService.addAlarmByUpdateTeamLevelWrite(id,scNo);//알람서비스
        }
        return "updateTeamLevel ok";
    }

    /**
     * 동행 삭제 (동행 삭제 + 알람서비스)
     * */
    // http://localhost:8080/teams/share/4 : teamMid=member
    // http://localhost:8080/teams/share/1 : teamMid=employee
    @DeleteMapping ("/teams/share/{scNo}")
    @ResponseBody
    public String deleteMyTeam(@PathVariable int scNo,@RequestBody Map<String,String> map) {
        String id = map.get("teamMid");
        teamScheduleService.removeTeam(scNo, id);
        alarmService.addAlarmByDeleteTeam(scNo,id); //알람서비스
        return "deleteMyTeam ok";
    }


    /**
     * 동행에게 초대 이메일 발송
     */
    // http://localhost:8080/teams/share/send-link/57  : teamId:jhla456@naver.com
    // http://localhost:8080/teams/share/send-link/58
    // http://localhost:8080/teams/share/send-link/59
    @PostMapping("/teams/share/send-link/{scNo}")
    @ResponseBody
    public String sendCode(@PathVariable int scNo, @RequestBody Map<String, String> map, HttpSession session) {
        String loginId = (String) session.getAttribute("loginId"); //초대하는 호스트 아이디
        String teamMid = map.get("teamMid"); //메세지 보낼 동행 아이디(이메일)

        String subject = ms.getMessage(
                "mail.share-code.subject",
                new Object[]{loginId},
                null);

        String message = ms.getMessage(
                "mail.share-code.message",
                new Object[]{"http://localhost:8080/teams/share/confirm/"+scNo+"/"+teamMid},
                null);

        EmailMessage emailMessage = EmailMessage.builder()
                .recipient(teamMid)
                .subject(subject)
                .message(message)
                .build();

        //이메일 전송
        emailSend.sendMail(emailMessage);
        log.info("email id={}", teamMid);
        log.info("subject={}", subject);
        log.info("message={}", message);
        return "OK";
    }

    /**
     * 이메일 받은 동행이 링크 클릭할 시 이동될 페이지 - teamShareConfirm.html
     * @param scNo
     * @param memId
     * @param model
     * @return
     */
    //http://localhost:8080/teams/share/confirm/111/jhla456@kakao.com
    @GetMapping("/teams/share/confirm/{scNo}/{memId}")
    public String showTeamShareAuth(@PathVariable int scNo, @PathVariable String memId, Model model) {
        model.addAttribute("memId", memId);
        model.addAttribute("scNo", scNo);
        return "teamShareConfirm";
    }


    @MessageMapping("/search") // 클라이언트가 '/app/search'로 메시지를 보내면 해당 메서드가 호출됨
    @SendTo("/topic/searchResults") // 서버가 '/topic/searchResults'를 구독하고 있는 클라이언트들에게 메시지 전송
    public String processSearch(String message) {
        // 검색 처리 로직 수행
        return "검색 결과: " + message;
    }

    @MessageMapping("/alert") // 클라이언트가 '/app/alert'로 메시지를 보내면 해당 메서드가 호출됨
    @SendTo("/topic/alerts") // 서버가 '/topic/alerts'를 구독하고 있는 클라이언트들에게 메시지 전송
    public String sendAlert(String message) {
        // 알림 전송 로직 수행
        return "알림: " + message;
    }


}
