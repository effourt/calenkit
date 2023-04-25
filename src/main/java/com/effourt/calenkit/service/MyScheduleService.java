package com.effourt.calenkit.service;

import com.effourt.calenkit.domain.Alarm;
import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.domain.Schedule;
import com.effourt.calenkit.domain.Team;
import com.effourt.calenkit.repository.*;
import lombok.Value;
import org.springframework.context.MessageSource;

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

    //[상세에서 내 스케줄 삭제(휴지통으로 이동) 행위] : goToRecycleBin()
    // => AlarmRepository.update
    // => ScheduleRepository.update
    // => AlarmRepository.save
    public void goToRecycleBin(Schedule schedule, Alarm alarm) {
        schedule.setScStatus(0);
        scheduleRepository.update(schedule);
        alarm.setAlStatus(0);
        alarmRepository.update(alarm);
        alarmRepository.save(alarm); //삭제 알림 출력 메세지 내용 properties 파일 - MessageSource, @Value
    }

    //[휴지통 출력]
    // =>
    // =>

    //[휴지통에서 내 스케줄 복원 행위]
    // => AlarmRepository.update
    // => ScheduleRepository.update
    public void restoreSchedule(Schedule schedule, Alarm alarm) {
        schedule.setScStatus(1);
        scheduleRepository.update(schedule);
        alarm.setAlStatus(1);
        alarmRepository.update(alarm);
    }

    //[휴지통에서 내 스케줄 완전 삭제 행위]
    // => TeamRepository.delete
    // => ScheduleRepository.delete
    // => AlarmRepository.delete
    public void removeSchedule(Integer scNo, String id, Integer alNo) {
       scheduleRepository.delete(scNo);
       teamRepository.delete(scNo, id);
       alarmRepository.delete(alNo);
    }

    // [내 스케줄 즐겨찾기 버튼 클릭 행위]
    // => 즐겨찾기 상태(teamBookmark) 변경
    public void addBookmark(Team team) {
        team.setTeamBookmark(1); //즐겨찾기 등록
        teamRepository.update(team);
    }

    // [권한 있는 일정 지정 연월 기준 출력]
    // => 현재 세션 아이디(loginMember) 기준 team, 출력 기준이 될 연월(date)을 매개변수로 입력받음
    public List<Schedule> getMySchedule(String id, String date) {
        //map 요소 : date, List 객체(일정번호) - map(date, "2020-12-12") , map(scNoList, List<Integer>)
        Map<String, Object> map=new HashMap<>();

        map.put("scNoList",teamRepository.findByMid(id));
        map.put("date", date); //date : 출력 기준이 될 연월(default:defaultDate)

        return scheduleRepository.findAllByScNo(map);
    }

    // [내 스케줄 즐겨찾기 출력]
    // => 현재 세션 아이디(loginMember) 기준 team, 출력 기준이 될 연월(date)을 매개변수로 입력받음
    public List<Schedule> getBookmark(String id, String date) {
        Map<String, Object> map=new HashMap<>();

        map.put("scNoList",teamRepository.findByBookmark(id));
        map.put("date", date); //date : 출력 기준이 될 연월(default:defaultDate)

        return scheduleRepository.findAllByScNo(map);
    }
}