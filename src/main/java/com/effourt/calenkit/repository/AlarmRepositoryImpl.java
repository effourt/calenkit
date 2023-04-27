package com.effourt.calenkit.repository;

import com.effourt.calenkit.domain.Alarm;
import com.effourt.calenkit.mapper.AlarmMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AlarmRepositoryImpl implements AlarmRepository {

    private final SqlSessionTemplate sqlSessionTemplate;

    @Override
    public Alarm save(Alarm alarm) {
        sqlSessionTemplate.getMapper(AlarmMapper.class).save(alarm);
        return alarm;
    }

    @Override
    public Integer update(Alarm alarm) {

        return sqlSessionTemplate.getMapper(AlarmMapper.class).update(alarm);
    }

    @Override
    public Integer delete(Integer alNo) {

        return sqlSessionTemplate.getMapper(AlarmMapper.class).delete(alNo);
    }

    @Override
    public List<Alarm> findByAlMid(String alMid) {
        return sqlSessionTemplate.getMapper(AlarmMapper.class).findByAlMid(alMid);
    }

    @Override
    public List<Alarm> findByAlScno(Integer AlScno) {
        return sqlSessionTemplate.getMapper(AlarmMapper.class).findByAlScno(AlScno);
    }
}
