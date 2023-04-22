package com.effourt.calenkit.service;

import com.effourt.calenkit.domain.Schedule;
import com.effourt.calenkit.domain.Team;
import com.effourt.calenkit.repository.AlarmRepository;
import com.effourt.calenkit.repository.ScheduleRepository;
import com.effourt.calenkit.repository.TeamRepository;
import com.effourt.calenkit.repository.TeamRepositoryImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyScheduleService {
    private TeamRepositoryImpl teamRepository;
    private ScheduleRepository scheduleRepository;
    private AlarmRepository alarmRepository;

    //[사이드바에서 내 스케줄 검색 행위] : findMySchedule()
    // => TeamRepository.find
    // => ScheduleRepository.find

    //[메인에서 내 스케줄 추가 행위] : addMySchedule()
    // => ScheduleRepository.save
    // => TeamRepository.save
    // => AlarmService.save

    //[메인이나 사이드바에서 내 스케줄 작성 행위] : modifyMySchedule()
    // => ScheduleRepository.update

    //[상세에서 내 스케줄 삭제 행위] : goToRecycleBin()
    // => TeamRepository.update
    // => AlarmRepository.update
    // => ScheduleRepository.update

    // [휴지통에서 내 스케줄 완전삭제 행위] : removeSchedule()
    // => TeamRepository.delete
    // => ScheduleRepository.delete
    // => AlarmRepository.delete
    public void removeSchedule(Team team) {
        teamRepository.findBySno();
        teamRepository.findByMid(team.getTeamMid());
    }

    // [내 스케줄 즐겨찾기 버튼 클릭 행위]
    // => 즐겨찾기 상태(teamBookmark) 변경
    public void addBookmark(Team team) {
        team.setTeamBookmark(1); //즐겨찾기 등록
        teamRepository.update(team);
    }

    // [권한 있는 일정 지정 연월 기준 출력]
    // => 현재 세션 아이디(loginMember) 기준 team, 출력 기준이 될 연월(date)을 매개변수로 입력받음
    public List<Schedule> getMySchedule(Team team, String date) {
        //map 요소 : date, List 객체(일정번호) - map(date, "2020-12-12") , map(scNoList, List<Integer>)
        Map<String, Object> map=new HashMap<>();

        map.put("scNoList",teamRepository.findByMid(team.getTeamMid()));
        map.put("date", date); //date : 출력 기준이 될 연월(default:defaultDate)

        return scheduleRepository.findAllByScNo(map);
    }

    // [내 스케줄 즐겨찾기 출력]
    // => 현재 세션 아이디(loginMember) 기준 team, 출력 기준이 될 연월(date)을 매개변수로 입력받음
    public List<Schedule> getBookmark(Team team, String date) {
        Map<String, Object> map=new HashMap<>();

        map.put("scNoList",teamRepository.findByBookmark(team.getTeamMid()));
        map.put("date", date); //date : 출력 기준이 될 연월(default:defaultDate)

        return scheduleRepository.findAllByScNo(map);
    }
}