package com.effourt.calenkit.service;

import com.effourt.calenkit.domain.Alarm;
import com.effourt.calenkit.domain.Schedule;
import com.effourt.calenkit.domain.Team;
import com.effourt.calenkit.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MyScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final TeamRepository teamRepository;
    private final AlarmRepository alarmRepository;

    //[사이드바에서 내 스케줄 검색 행위]
    public List<Schedule> findMySchedule(String id, String keyword) {
        Map<String, Object> map=new HashMap<>();
        map.put("ScNo", teamRepository.findByMid(id));
        map.put("keyword", keyword);

        return scheduleRepository.findAllByScNo(map);
    }

    //[메인에서 내 스케줄 추가 행위]
    @Transactional
    public Integer addMySchedule(String id) {
        scheduleRepository.save();
        Integer scNo=scheduleRepository.findLastInsertScNo();

        Team team=new Team();
        team.setTeamMid(id);
        team.setTeamSno(scNo);
        teamRepository.save(team);
        return scNo;
    }

    //[메인이나 사이드바에서 내 스케줄 작성 행위] : modifyMySchedule()
    // => ScheduleRepository.update
    public void writeMySchdule(Schedule schedule) {
        scheduleRepository.update(schedule);
    }

    //[휴지통 출력]
    // => 팝업창에서 출력(10개씩 출력, 스크롤 로딩)
    // => 검색어(keyword)가 없을 경우 null로 전달 받아야 함(필수 매개변수)
    public List<Schedule> getRecycleBin(String id, String keyword) {
        Map<String, Object> map=new HashMap<>();

        map.put("scNoList", teamRepository.findByMid(id));
        map.put("keyword", keyword);

        return scheduleRepository.findByRecycleBin(map);
    }

    //[상세에서 내 스케줄 삭제(휴지통으로 이동) 행위] : goToRecycleBin()
    @Transactional
    public void goToRecycleBin(Integer scNo) {
        Schedule schedule=scheduleRepository.findByScNo(scNo);
        schedule.setScStatus(0);
        scheduleRepository.update(schedule);
    }

    //[휴지통에서 내 스케줄 복원 행위]
    @Transactional
    public void restoreSchedule(Schedule schedule, Alarm alarm) {
        schedule.setScStatus(1);
        scheduleRepository.update(schedule);
        alarm.setAlStatus(1);
        alarmRepository.update(alarm);
    }

    //[휴지통에서 내 스케줄 완전 삭제 행위]
    @Transactional
    public void removeSchedule(Integer scNo, String id, Integer alNo) {
       scheduleRepository.delete(scNo);
       teamRepository.delete(scNo, id);
       alarmRepository.delete(alNo);
    }

    // [내 스케줄 즐겨찾기 버튼 클릭 행위]
    // => 즐겨찾기 상태(teamBookmark) 변경
    @Transactional
    public void addBookmark(Team team) {
        team.setTeamBookmark(1); //즐겨찾기 등록
        teamRepository.update(team);
    }

    // [권한 있는 일정 지정 연월 기준 출력]
    // => 현재 세션 아이디(loginMember) 기준 team, 출력 기준이 될 연월(date)을 매개변수로 입력받음
    public List<Schedule> getMySchedule(String id, String date) {
        //map 요소 : date, List 객체(일정번호) - map(date, "2020-12-12") , map(scNoList, List<Integer>)
        Map<String, Object> map=new HashMap<>();

        map.put("scNoList", teamRepository.findByid(id)); //아이디 기준 권한있는 일정번호목록
        map.put("date", date); //date : 출력 기준이 될 연월(default:현재 연월)

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