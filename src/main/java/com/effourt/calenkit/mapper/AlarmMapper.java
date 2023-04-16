package com.effourt.calenkit.mapper;

import com.effourt.calenkit.domain.Alarm;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AlarmMapper {

    Integer save(Alarm alarm);
    Integer update(Alarm alarm);
    Integer delete(Integer alNo);
    Alarm findByAlNo(Integer alNo);
    List<Alarm> findByAlMid(String alMid);
}
