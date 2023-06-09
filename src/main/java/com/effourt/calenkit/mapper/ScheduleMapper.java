package com.effourt.calenkit.mapper;

import com.effourt.calenkit.domain.Schedule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

import java.util.List;

@Mapper
public interface ScheduleMapper {

    Integer save(String date);
    Integer update(Schedule schedule);
    Integer updateStatus(Schedule schedule);
    Integer delete(Integer scNo);
    Integer findLastInsertScNo();
    Schedule findByScNo(Integer scNo);

    //map 요소 : date, List 객체(일정번호) - map(date, "2020-12-12") , map(scNoList, List<Integer>)
    //map.put("date", "2020-10")
    //map.put("scNoList",new ArrayList<Integer>().add(1));
    List<Schedule> findAllByScNo(Map<String, Object> map);
    //map 요소 : keyword, List 객체(일정번호)
    List<Schedule> findByRecycleBin(Map<String, Object> map);
    //map 요소 : keyword, Filter, List 객체(일정번호)
    List<Schedule> findByFilter(Map<String, Object> map);

    Integer countFindByRecycleBin(Map<String, Object> map);
    Integer countFindByFilter(Map<String, Object> map);
    Integer countFindAllByScNo(List<Integer> scNoList);
}