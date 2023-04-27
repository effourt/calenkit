package com.effourt.calenkit.repository;

import com.effourt.calenkit.domain.Alarm;

import java.util.List;

public interface AlarmRepository {
    Alarm save(Alarm alarm);
    Integer update(Alarm alarm);
    Integer delete(Integer alNo);
    List<Alarm> findByAlMid(String alMid);
    List<Alarm> findByAlScno(Integer AlScno);
}
