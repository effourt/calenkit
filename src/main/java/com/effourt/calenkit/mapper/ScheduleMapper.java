package com.effourt.calenkit.mapper;

import com.effourt.calenkit.domain.Schedule;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ScheduleMapper {

    Integer save(Schedule schedule);
    Integer update(Schedule schedule);
    Integer delete(Integer scNo);
}
