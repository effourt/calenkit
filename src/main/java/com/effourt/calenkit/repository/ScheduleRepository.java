package com.effourt.calenkit.repository;

import com.effourt.calenkit.domain.Schedule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


public interface ScheduleRepository {
    Schedule save(Schedule schedule);
    Integer update(Schedule schedule);
    Integer delete(Integer scNo);
    List<Schedule> findAllByScNo(Map<String, Object> map);
    List<Schedule> findByStatus(Map<String, Object> map);
    List<Schedule> findByFilter(List<Integer> scNoList, String filter, String keyword);
}
