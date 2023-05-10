package com.effourt.calenkit.repository;

import com.effourt.calenkit.domain.Alarm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AlarmRepository {
    Alarm save(Alarm alarm);
    Integer update(Alarm alarm);
    Integer delete(String alMid,Integer alScno);
    Alarm findByAlNo(Integer alNo);
    List<Alarm> findByAlMid(String alMid);
    List<Alarm> findByAlScno(Integer slScno);
}
