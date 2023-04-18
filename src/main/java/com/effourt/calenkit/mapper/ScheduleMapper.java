package com.effourt.calenkit.mapper;

import com.effourt.calenkit.domain.Schedule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ScheduleMapper {

    Integer save(Schedule schedule);
    Integer update(Schedule schedule);
    Integer delete(Integer scNo);
}
