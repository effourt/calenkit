package com.effourt.calenkit.repository;

import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.mapper.AlarmMapper;
import com.effourt.calenkit.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final SqlSessionTemplate sqlSessionTemplate;

    @Override
    public Integer save(Member member) {
        return sqlSessionTemplate.getMapper(MemberMapper.class).save(member);
    }

    @Override
    public Integer updatePassword(Member member) {
        return sqlSessionTemplate.getMapper(MemberMapper.class).updatePassword(member);
    }
    @Override
    public Integer update(Member member) {
        return sqlSessionTemplate.getMapper(MemberMapper.class).update(member);
    }
    @Override
    public Integer updateStatus(Member member) {
        return sqlSessionTemplate.getMapper(MemberMapper.class).updateStatus(member);
    }

    @Override
    public Integer delete(String memId) {
        return sqlSessionTemplate.getMapper(MemberMapper.class).delete(memId);
    }

    @Override
    public Member findByMemId(String memId) {
        return sqlSessionTemplate.getMapper(MemberMapper.class).findByMemId(memId);
    }

    @Override
    public int findByMemName(String memName) {
        return sqlSessionTemplate.getMapper(MemberMapper.class).findByMemName(memName);
    }

    @Override
    public List<Member> findAllByMemId(String keyword) {
        return sqlSessionTemplate.getMapper(MemberMapper.class).findAllByMemId(keyword);
    }
}
