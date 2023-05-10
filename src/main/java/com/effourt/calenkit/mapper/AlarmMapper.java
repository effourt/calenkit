package com.effourt.calenkit.mapper;

import com.effourt.calenkit.domain.Alarm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlarmMapper {
    Integer save(Alarm alarm);
    Integer update(Alarm alarm);
    Integer delete(@Param("alMid") String alMid, @Param("alScno") Integer alScno);
    Alarm findByAlNo(Integer alNo);
    List<Alarm> findByAlMid(String alMid);
    List<Alarm> findByAlScno(Integer alScno);
}
