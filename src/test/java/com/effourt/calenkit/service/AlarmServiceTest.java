package com.effourt.calenkit.service;

import com.effourt.calenkit.domain.Alarm;
import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.domain.Schedule;
import com.effourt.calenkit.domain.Team;
import com.effourt.calenkit.repository.AlarmRepository;
import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Transactional
@SpringBootTest
public class AlarmServiceTest {

    @Autowired
    private AlarmService alarmService;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private AlarmRepository alarmRepository;

//    @DisplayName("일정추가_시_울릴_알람서비스_테스트")
//    @Test
//    void addAlarm() {
//        //given
//        Team team1 = new Team();
//        Team team2 = new Team();
//        team1.setTeamSno(2);//스케줄번호 세팅
//        team2.setTeamSno(2);
//        team1.setTeamMid("member");//아이디 세팅
//        team2.setTeamMid("employee");
//        team1.setTeamLevel(1);//권한레벨 세팅
//        team2.setTeamLevel(1);
//        teamRepository.save(team1);//레파지토리에 저장
//        teamRepository.save(team2);
//
//        //when - 서비스메소드 호출
//        alarmService.addAlarmByModifySchedule(2);
//
//        //then
//        List<Alarm> findAlarmList = alarmRepository.findByAlScno(1);
//        System.out.println(findAlarmList.get(0).getAlMid());
//        System.out.println(findAlarmList.get(1).getAlMid());
//        System.out.println(findAlarmList.get(2).getAlMid());
//        System.out.println(findAlarmList.size());
//    }

    @DisplayName("AlarmService 시스템 메소드 테스트")
    @Test
    void findIdListTest() {
            Team team1 = new Team();
            Team team2 = new Team();
            Team team3 = new Team();

            team1.setTeamMid("abc123");
            team2.setTeamMid("xyz123");
            team3.setTeamMid("qwe123");
            team1.setTeamSno(1);
            team2.setTeamSno(1);
            team3.setTeamSno(1);

            List<Team> teamList = new ArrayList<>();
            teamList.add(team1);
            teamList.add(team2);
            teamList.add(team3);

            String[] idList = new String[teamList.size()]; //team의 id만 담을 배열 초기화
            for (int i = 0; i < teamList.size(); i++) {
                idList[i] = teamList.get(i).getTeamMid();
            }
            System.out.println(idList.length);
            System.out.println(idList[0]);
            System.out.println(idList[1]);
            System.out.println(idList[2]);
        }
}
