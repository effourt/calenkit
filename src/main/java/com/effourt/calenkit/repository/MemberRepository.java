package com.effourt.calenkit.repository;

import com.effourt.calenkit.domain.Member;

import java.util.List;

public interface MemberRepository {
    Integer save(Member member);
    Integer updatePassword(Member member);
    Integer update(Member member);
    Integer updateStatus(Member member);
    Integer delete(String memId);
    Member findByMemId(String memId);
    int findByMemName(String memName);
    List<Member> findAllByMemId(String keyword);
}
