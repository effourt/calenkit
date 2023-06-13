package com.effourt.calenkit.service;

import com.effourt.calenkit.domain.Alarm;
import com.effourt.calenkit.domain.Schedule;
import com.effourt.calenkit.domain.Team;
import com.effourt.calenkit.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MyScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final TeamRepository teamRepository;

    //[메인에서 내 스케줄 추가 행위]
    @Transactional
    public Integer addMySchedule(String id, String date) {
        if(date==null || date.isEmpty() || date.isBlank()) { //날짜 미지정 시 현재 날짜로 설정
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            Date temp=new Date(System.currentTimeMillis());
            date=simpleDateFormat.format(temp);
        }

        scheduleRepository.save(date);
        Integer scNo=scheduleRepository.findLastInsertScNo();

        Team team=new Team();
        team.setTeamMid(id);
        team.setTeamSno(scNo);
        team.setTeamLevel(9);
        teamRepository.save(team);
        return scNo;
    }

    //[휴지통 출력]
    // => 팝업창에서 출력(10개씩 출력, 스크롤 로딩)
    // => 검색어(keyword)가 없을 경우 null로 전달 받아야 함(필수 매개변수)
    public List<Schedule> getRecycleBin(String id, String keyword, String filter, Integer startRowNum, Integer rowCount) {
        Map<String, Object> map=new HashMap<>();
        List<Integer> scNoList=teamRepository.findByid(id);
        if(scNoList.isEmpty()) { //조건에 만족하는 일정이 없을 경우 미출력(scNo=0)
            scNoList.add(0);
        }

        map.put("scNoList", scNoList);
        map.put("keyword", keyword);
        map.put("filter", filter);
        map.put("startRowNum", startRowNum);
        map.put("rowCount", rowCount);

        return scheduleRepository.findByRecycleBin(map);
    }

    //[상세에서 내 스케줄 삭제(휴지통으로 이동) 행위] : goToRecycleBin()
    @Transactional
    public void goToRecycleBin(Integer scNo) {
        Schedule schedule=scheduleRepository.findByScNo(scNo);
        if(schedule.getScStatus()!=0) {
            schedule.setScStatus(0);
        }
        scheduleRepository.updateStatus(schedule);
    }

    //[휴지통에서 내 스케줄 복원 행위]
    @Transactional
    public void restoreSchedule(Integer scNo) {
        Schedule schedule=scheduleRepository.findByScNo(scNo);
        if(schedule.getScStatus()!=1) {
            schedule.setScStatus(1);
        }
        scheduleRepository.updateStatus(schedule);
    }

    //[휴지통에서 내 스케줄 완전 삭제 행위]
    @Transactional
    public void removeSchedule(Integer scNo, String id) {
       teamRepository.delete(scNo, id);
       scheduleRepository.delete(scNo);
    }

    // [내 스케줄 즐겨찾기 버튼 클릭 행위]
    // => 즐겨찾기 상태(teamBookmark) 변경
    @Transactional
    public void updateBookmark(Integer scNo, String id) {
        Team team=teamRepository.findBySnoAndMid(scNo, id);
        if(team.getTeamBookmark()==0) {
            team.setTeamBookmark(1); //즐겨찾기 등록
        } else {
            team.setTeamBookmark(0); //즐겨찾기 해제
        }
        teamRepository.update(team);
    }

    // [권한 있는 일정 지정 연월 기준 출력]
    // => 현재 세션 아이디(loginMember) 기준 team, 출력 기준이 될 연월(date)을 매개변수로 입력받음
    public List<Schedule> getMySchedule(String id, String date, Integer startRowNum, Integer rowCount) {
        //map 요소 : date, List 객체(일정번호) - map(date, "2020-12-12") , map(scNoList, List<Integer>)
        Map<String, Object> map=new HashMap<>();
        List<Integer> scNoList=teamRepository.findByid(id);
        if(scNoList.isEmpty()) { //조건에 만족하는 일정이 없을 경우 미출력(scNo=0)
            scNoList.add(0);
        }

        map.put("scNoList", scNoList); //아이디 기준 권한있는 일정번호목록
        map.put("date", date); //date : 출력 기준이 될 연월(default:현재 연월)
        map.put("startRowNum", startRowNum);
        map.put("rowCount", rowCount);

        return scheduleRepository.findAllByScNo(map);
    }

    // [내 스케줄 즐겨찾기 출력]
    // => 현재 세션 아이디(loginMember) 기준 team, 출력 기준이 될 연월(date)을 매개변수로 입력받음
    public List<Schedule> getBookmark(String id, String date, Integer startRowNum, Integer rowCount) {
        Map<String, Object> map=new HashMap<>();
        List<Integer> scNoList=teamRepository.findByBookmark(id);
        if(scNoList.isEmpty()) { //조건에 만족하는 일정이 없을 경우 미출력(scNo=0)
            scNoList.add(0);
        }

        map.put("scNoList", scNoList);
        map.put("date", date); //date : 출력 기준이 될 연월(default:defaultDate)
        map.put("startRowNum", startRowNum);
        map.put("rowCount", rowCount);

        return scheduleRepository.findAllByScNo(map);
    }

    public List<Schedule> searchSchedule(String id, String keyword, String filter, Integer startRowNum, Integer rowCount) {
        Map<String, Object> map=new HashMap<>();
        List<Integer> scNoList=teamRepository.findByid(id);
        if(scNoList.isEmpty()) { //조건에 만족하는 일정이 없을 경우 미출력(scNo=0)
            scNoList.add(0);
        }

        map.put("scNoList", scNoList);
        map.put("keyword", keyword);
        map.put("filter", filter);
        map.put("startRowNum", startRowNum);
        map.put("rowCount", rowCount);

        return scheduleRepository.findByFilter(map);
    }

    public Integer countRecyclebin(String id, String keyword, String filter) {
        Map<String, Object> map=new HashMap<>();
        List<Integer> scNoList=teamRepository.findByid(id);
        if(scNoList.isEmpty()) { //조건에 만족하는 일정이 없을 경우 미출력(scNo=0)
            scNoList.add(0);
        }

        map.put("scNoList", scNoList);
        map.put("keyword", keyword);
        map.put("filter", filter);

        return scheduleRepository.countFindByRecycleBin(map);
    }

    public Integer countBookmark(String id) {
        List<Integer> scNoList=teamRepository.findByBookmark(id);
        if(scNoList.isEmpty()) { //조건에 만족하는 일정이 없을 경우 미출력(scNo=0)
            scNoList.add(0);
        }

        return scheduleRepository.countFindAllByScNo(scNoList);
    }
}