package com.effourt.calenkit.controller;

import com.effourt.calenkit.domain.Alarm;
import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.dto.EmailMessage;
import com.effourt.calenkit.dto.TeamShare;
import com.effourt.calenkit.exception.ExistsTeamException;
import com.effourt.calenkit.exception.MemberNotFoundException;
import com.effourt.calenkit.exception.ScheduleNotFoundException;
import com.effourt.calenkit.exception.TeamNotFoundException;
import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.service.AlarmService;
import com.effourt.calenkit.service.TeamScheduleService;
import com.effourt.calenkit.util.EmailSend;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/teams/share")
@RequiredArgsConstructor
public class TeamController {

    private final TeamScheduleService teamScheduleService;
    private final AlarmService alarmService;
    private final MemberRepository memberRepository;
    private final MessageSource ms;
    private final EmailSend emailSend;

    /**
     * 이메일 받은 동행이 링크 클릭할 시 이동될 페이지 - teamShareConfirm.html
     */
    //http://localhost:8080/teams/share/confirm/111/jhla456@kakao.com
    @GetMapping("/confirm/{scNo}/{memId}")
    public String moveTeamShareConfirm(@PathVariable int scNo, @PathVariable String memId, Model model) {
        model.addAttribute("memId", memId);
        model.addAttribute("scNo", scNo);
        return "calendar/teamShareConfirm";
    }

    /**
     * 동행에게 초대 이메일 발송
     */
    // http://localhost:8080/teams/share/send-link/57  : teamId:jhla456@naver.com
    @PostMapping("/send-link/{scNo}")
    @ResponseBody
    public String sendEmail(@PathVariable int scNo, @RequestBody Map<String, String> map, HttpSession session) {
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
     * 회원 조회 (실시간 회원 검색)
     */
    // http://localhost:8080/teams/share/members?memId=Test3@test3.com
    @GetMapping("/members")
    @ResponseBody
    public Member getMemberForShare(@RequestParam String memId) {
        return memberRepository.findByMemId(memId);
    }

    /**
     * 동행 조회
     */
    // http://localhost:8080/teams/share/1
    @GetMapping ("/{scNo}")
    @ResponseBody
    public List<TeamShare> searchMyTeam(@PathVariable int scNo) throws TeamNotFoundException, ScheduleNotFoundException{
        return teamScheduleService.getTeam(scNo);
    }

    /**
     * 동행 추가 (동행추가 + 알람서비스)
     */
    // http://localhost:8080/teams/share/1  : memId=test@test.com
    @PostMapping ("/{scNo}")
    @ResponseBody
    public String addTeam(@PathVariable int scNo, @RequestBody Map<String,Object> map, HttpSession session) throws ExistsTeamException, MemberNotFoundException, ScheduleNotFoundException{
        String loginId = (String) session.getAttribute("loginId");
        String memId = (String)map.get("memId");

        log.info("[shareTeam] scNo = {}",scNo);
        log.info("[shareTeam] memId = {}",memId);
        log.info("[shareTeam] loginId = {}",loginId);

        if(loginId.equals(memId)){
            teamScheduleService.addTeam(scNo,memId);
            Alarm alarm = alarmService.addAlarmBySaveTeam(scNo,memId); //알람서비스
            log.info("[shareTeam] ok");
            return "ok-add-team";
        } else{
            log.info("[shareTeam] login-again");
            return "fail-login-again";
        }
    }

    /**
     * 동행의 권한 상태 변경 (권한상태변경 + 알람서비스)
     * teamLevel은 무조건 - 읽기권한:0, 수정권한:1
     */
    // http://localhost:8080/teams/share/1  : teamMid=member?teamLevel=0
    @PatchMapping("/{scNo}")
    @ResponseBody
    public String updateTeamByTeamLevel(@PathVariable int scNo,@RequestBody Map<String,Object> map) throws TeamNotFoundException, ScheduleNotFoundException {
        String id = (String)map.get("teamMid");
        int level = Integer.parseInt(String.valueOf(map.get("teamLevel"))); //String으로 변환한 후 Integer.parseInt
        teamScheduleService.modifyTeamLevel(scNo,id,level);
        if(level==0){ //읽기
            alarmService.addAlarmByUpdateTeamLevelRead(scNo,id);//알람서비스
        } else if(level==1){ //수정
            alarmService.addAlarmByUpdateTeamLevelWrite(scNo,id);//알람서비스
        }
        return "ok-updateTeamLevel";
    }

    /**
     * 동행 삭제 (동행 삭제 + 알람서비스)
     */
    // http://localhost:8080/teams/share/4 : teamMid:jhla456@naver.com
    @DeleteMapping ("/{scNo}")
    @ResponseBody
    public String deleteTeam(@PathVariable int scNo,@RequestBody Map<String,String> map) throws ScheduleNotFoundException, TeamNotFoundException {
        String id = map.get("teamMid");
        teamScheduleService.removeTeam(scNo, id);
        alarmService.addAlarmByDeleteTeam(scNo,id); //알람서비스
        return "deleteMyTeam ok";
    }

}
