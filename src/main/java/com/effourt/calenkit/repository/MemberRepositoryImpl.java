package com.effourt.calenkit.repository;

import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final SqlSessionTemplate sqlSessionTemplate;

    @Override
    public Integer save(Member member) {
        return sqlSessionTemplate.getMapper(MemberMapper.class).save(member);
    }
}
