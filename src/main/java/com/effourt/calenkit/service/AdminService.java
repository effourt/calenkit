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
    public void modifyPassword(Member member) throws MemberNotFoundException{
        if(memberRepository.findByMemId(member.getMemId())==null) {
            throw new MemberNotFoundException("아이디의 회원정보가 존재하지 않습니다.");
        }
        //전달받은 회원정보의 상태가 존재할 경우, 상태 필드값 변경
        if(member.getMemStatus()!=null && !member.getMemStatus().equals("")) {
            member.setMemStatus(member.getMemStatus());
        }
        memberRepository.update(member);
    }

    // [회원 말소 행위] : removeMember()
    // => MemberRepository.delete
    @Transactional
    public void removeMember(String memId) throws MemberNotFoundException{

            memberRepository.delete(memId);
    }
}