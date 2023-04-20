package com.effourt.calenkit.service;


import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.exception.MemberNotFoundException;
import com.effourt.calenkit.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final MemberRepository memberRepository;
    // [내정보 변경 행위] : modifyMe()
    // => MemberRepository.find
    // => MemberRepository.update
    @Transactional
    public void modifyMe(Member member) throws MemberNotFoundException {
        if(memberRepository.findByMemId(member.getMemId())==null) {
            throw new MemberNotFoundException("아이디의 회원정보가 존재하지 않습니다.");
        }
        //전달받은 회원정보의 비밀번호가 존재할 경우 암호화된 비밀번호로 필드값 변경
        if(member.getMemPw()!=null && !member.getMemPw().equals("")) {
            member.setMemPw(BCrypt.hashpw(member.getMemPw(),BCrypt.gensalt()));
        }
        //전달받은 회원정보의 프로필이 존재할 경우 필드값 변경
        if(member.getMemImage()!=null && !member.getMemImage().equals("")){
            member.setMemImage(member.getMemImage());
        }
        member.setMemName(member.getMemName());
        member.setMemStatus(member.getMemStatus());
        member.setMemAuthId(member.getMemAuthId());
        memberRepository.update(member);
    }


    // [비밀번호 변경 행위] : modifyPassword()
    // => MemberRepository.find
    // => EmailSendService
    // => MemberRepository.update

    @Transactional
    public void modifyPassword(Member member) throws MemberNotFoundException{
        if(memberRepository.findByMemId(member.getMemId())==null) {
            throw new MemberNotFoundException("아이디의 회원정보가 존재하지 않습니다.");
        }
        //전달받은 회원정보의 비밀번호가 존재할 경우 암호화된 비밀번호로 필드값 변경
        if(member.getMemPw()!=null && !member.getMemPw().equals("")) {
            member.setMemPw(BCrypt.hashpw(member.getMemPw(),BCrypt.gensalt()));
        }
        memberRepository.updatePassword(member);
    }
    // [회원탈퇴 행위] : removeMe()
    // => MemberRepository.update
    public void removeMe(Member member) throws MemberNotFoundException{
        if(memberRepository.findByMemId(member.getMemId())==null) {
            throw new MemberNotFoundException("아이디의 회원정보가 존재하지 않습니다.");
        }
        //전달받은 회원정보의 상태가 존재할 경우 상태 필드값 변경
        if(member.getMemStatus()!=null) {
            member.setMemStatus(member.getMemStatus());
        }
        memberRepository.update(member);
    }
    // [로그아웃 행위] : logout()
    // => 세션 값 삭제
    public void removeMe(HttpSession session){

        session.invalidate();
    }
}