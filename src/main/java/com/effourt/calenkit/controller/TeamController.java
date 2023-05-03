package com.effourt.calenkit.controller;

import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.domain.Team;
import com.effourt.calenkit.dto.TeamShare;
import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.repository.ScheduleRepository;
import com.effourt.calenkit.repository.TeamRepository;
import com.effourt.calenkit.service.TeamScheduleService;
import com.effourt.calenkit.util.EmailSend;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@Slf4j
@Controller
@RequiredArgsConstructor
public class TeamController {

    private final TeamScheduleService teamScheduleService;
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
     * 동행 추가
     * */
    // http://localhost:8080/teams/share/1  : memId=member
    // http://localhost:8080/teams/share/1  : memId=test@test.com
    // http://localhost:8080/teams/share/1  : memId=employee

    @PostMapping ("/teams/share/{scNo}")
    @ResponseBody
    public Team shareTeam(@PathVariable int scNo, @RequestBody Map<String,Object> map) {
        Team newTeam = teamScheduleService.addTeam(scNo,(String)map.get("memId"));

        if(newTeam.equals("") || newTeam==null){ //삽입 실패
            return null; //예외처리
        }
        return newTeam;
    }


    /***
     * 동행의 권한 상태 변경
     * @param map -> teamLevel은 무조건 - 읽기권한:0, 수정권한:1
     * @return
     */
    // http://localhost:8080/teams/share/1  : teamMid=member?teamLevel=0
    // http://localhost:8080/teams/share/1  : teamMid=member?teamLevel=1
    @PatchMapping("/teams/share/{scNo}")
    @ResponseBody
    public String updateTeamLevel(@PathVariable int scNo,@RequestBody Map<String,Object> map) {
        String id = (String)map.get("teamMid");
        //int level = (int)map.get("teamLevel"); //error
        int level = Integer.parseInt(String.valueOf(map.get("teamLevel"))); //String으로 변환한 후 Integer.parseInt
        teamScheduleService.modifyTeamLevel(scNo,id,level);
        return "updateTeamLevel ok";
    }

    /**
     * 동행 삭제
     * */
    // http://localhost:8080/teams/share/4 : teamMid=member
    // http://localhost:8080/teams/share/1 : teamMid=employee
    @DeleteMapping ("/teams/share/{scNo}")
    @ResponseBody
    public int deleteMyTeam(@PathVariable int scNo,@RequestBody Map<String,String> map) {
        return teamScheduleService.removeTeam(scNo, map.get("teamMid"));
    }


    /**
     * 동행에게 초대 이메일 발송
     */
    // http://localhost:8080/teams/send/email
    /*
    @PostMapping("/teams/send/email")
    @ResponseBody
    public String sendCode(@RequestBody Map<String, String> idMap, HttpSession session) {
        String memId = idMap.get("id");
        String subject = ms.getMessage("mail.login-code.subject", null, null);
        String message = ms.getMessage(
                "mail.login-code.message",
                new Object[]{emailSend.createAccessCode(memId, session)},
                null);

        EmailMessage emailMessage = EmailMessage.builder()
                .recipient(memId)
                .subject(subject)
                .message(message)
                .build();
        //이메일 전송
        emailSend.sendMail(emailMessage);

        log.info("email id={}", memId);
        log.info("subject={}", subject);
        log.info("message={}", message);
        return "OK";
    }
    */



}
