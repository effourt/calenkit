package com.effourt.calenkit.repository;

import com.effourt.calenkit.domain.Schedule;
import com.effourt.calenkit.mapper.MemberMapper;
import com.effourt.calenkit.mapper.ScheduleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Slf4j
@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepository{

    private final SqlSessionTemplate sqlSessionTemplate;

    @Override
    public Schedule save(Schedule schedule) {
        sqlSessionTemplate.getMapper(ScheduleMapper.class).save(schedule);
        return schedule;
    }

    @Override
    public Integer update(Schedule schedule) {
        return sqlSessionTemplate.getMapper(ScheduleMapper.class).update(schedule);
    }

    @Override
    public Integer delete(Integer scNo) {
        return sqlSessionTemplate.getMapper(ScheduleMapper.class).delete(scNo);
    }

    @Override
    public List<Schedule> findAllByScNo(Map<String, Object> map) {
        return sqlSessionTemplate.getMapper(ScheduleMapper.class).findAllByScNo(map);
    }

    @Override
    public List<Schedule> findByStatus(Map<String, Object> map) {
        return sqlSessionTemplate.getMapper(ScheduleMapper.class).findByStatus(map);
    }

    @Override
    public List<Schedule> findByFilter(List<Integer> scNoList, String filter, String keyword) {
        return sqlSessionTemplate.getMapper(ScheduleMapper.class).findByFilter(scNoList, filter, keyword);
    }
}
