package com.effourt.calenkit.service;

import com.effourt.calenkit.domain.Alarm;
import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.domain.Schedule;
import com.effourt.calenkit.domain.Team;
import com.effourt.calenkit.exception.MemberNotFoundException;
import com.effourt.calenkit.repository.AlarmRepository;
import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.repository.ScheduleRepository;
import com.effourt.calenkit.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final ScheduleRepository scheduleRepository;

    private final AlarmRepository alarmRepository;
    private final AlarmService alarmService;
    private final MyScheduleService myScheduleService;

    // [회원 검색 행위] : findMember()
    // => MemberRepository.find
    @Transactional
    public void findMember(String memId) throws MemberNotFoundException {
        if(memberRepository.findByMemId(memId)==null) {
            throw new MemberNotFoundException("아이디의 회원정보가 존재하지 않습니다.");
        }
        memberRepository.findByMemId(memId);
    }
    // [회원 상태 변경 행위] : modifyMember()
    // => MemberRepository.delete
    @Transactional
    public void modifyStatus(Member member){

        memberRepository.updateStatus(member);
    }

    // [회원 말소 행위] : removeMember()
    // => MemberRepository.delete
    @Transactional
    public void removeMember(String memId){

        //멤버 통해서 일정 검색
        List<Integer> scNoList = teamRepository.findByid(memId);

        //멤버의 모든 일정 검색
        for (Integer scNo : scNoList) {
            Team myTeam=teamRepository.findBySnoAndMid(scNo,memId);
            System.out.println("before.scNoList="+scNoList);
            //스케쥴 번호로 검색 일정 팀 권한이 9가 아닐 경우 본인정보만 삭제
            if(myTeam.getTeamLevel()!=9) {
                //알림 삭제(일정 번호 참조하여 알림 삭제)
                alarmRepository.delete(memId, scNo);
                //권한 삭제(일정 번호 참조하여 권한 삭제 일반일 경우(String), 호스트일 경우(List)삭제).
                teamRepository.delete(scNo, memId);
                //일정 삭제(일정 번호 리스트)
                scheduleRepository.delete(scNo);
            }

            //스케쥴 번호로 검색 일정 팀 권한이 9일 경우 본인+팀정보 삭제
            else{

                //알림 삭제(일정 번호 참조하여 권한 삭제 일반일 경우(String), 호스트일 경우(List)삭제).
                List<Alarm> teamAlarm=alarmRepository.findByAlScno(scNo);
                System.out.println("before.teamAlarm="+teamAlarm);
                for (Alarm alarm : teamAlarm) {
                    alarmRepository.delete(alarm.getAlMid(),scNo);
                }
                System.out.println("after.teamAlarm="+teamAlarm);


                //권한 삭제(일정 번호 참조하여 권한 삭제 일반일 경우(String), 호스트일 경우(List)삭제).
                //일정 삭제(일정 번호 리스트)
                List<Team> teamMember=teamRepository.findBySno(scNo);
                System.out.println("before.teamMember="+teamMember);
                for (Team team : teamMember) {
                    teamRepository.delete(scNo,team.getTeamMid());
                    scheduleRepository.delete(scNo);
                }
                System.out.println("after.teamMember="+teamMember);
                System.out.println("after.scNoList="+scNoList);
            }
        }

        //멤버 삭제
        memberRepository.delete(memId);
    }


}