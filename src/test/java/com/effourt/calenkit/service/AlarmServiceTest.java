package com.effourt.calenkit.service;

import com.effourt.calenkit.domain.Alarm;
import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.domain.Schedule;
import com.effourt.calenkit.domain.Team;
import com.effourt.calenkit.repository.AlarmRepository;
import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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

    @Test
    void 일정추가_시_울릴_알람서비스_테스트() {
        //given
        Team team1 = new Team();
        Team team2 = new Team();
        team1.setTeamSno(2);//스케줄번호 세팅
        team2.setTeamSno(2);
        team1.setTeamMid("member");//아이디 세팅
        team2.setTeamMid("employee");
        team1.setTeamLevel(1);//권한레벨 세팅
        team2.setTeamLevel(1);
        teamRepository.save(team1);//레파지토리에 저장
        teamRepository.save(team2);

        //when - 서비스메소드 호출
        alarmService.addAlarmByModifySchedule(2);

        //then
        List<Alarm> findAlarmList = alarmRepository.findByAlScno(1);
        System.out.println(findAlarmList.get(0).getAlMid());
        System.out.println(findAlarmList.get(1).getAlMid());
        System.out.println(findAlarmList.get(2).getAlMid());
        System.out.println(findAlarmList.size());
    }
}
