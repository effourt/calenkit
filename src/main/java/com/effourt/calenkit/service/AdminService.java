package com.effourt.calenkit.service;

import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.exception.MemberNotFoundException;
import com.effourt.calenkit.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final MemberRepository memberRepository;
    // [회원 검색 행위] : findMember()
    // => MemberRepository.find
    @Transactional
    public void findMember(String memId) throws MemberNotFoundException {
        if(memberRepository.findByMemId(memId)==null) {
            throw new MemberNotFoundException("아이디의 회원정보가 존재하지 않습니다.");
        }
        memberRepository.findByMemId(memId);
    }
    // [회원 상태 변경 행위] : modifyMember()
    // => MemberRepository.delete
    @Transactional
    public void modifyMember(Member member) throws MemberNotFoundException{
        Member selectMember=memberRepository.findByMemId(member.getMemId());
        selectMember.setMemStatus(member.getMemStatus());
        memberRepository.updateStatus(selectMember);
    }

    // [회원 말소 행위] : removeMember()
    // => MemberRepository.delete
    @Transactional
    public void removeMember(String memId) throws MemberNotFoundException{
            memberRepository.delete(memId);
    }




}