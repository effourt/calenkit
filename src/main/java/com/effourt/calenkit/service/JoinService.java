package com.effourt.calenkit.service;

import com.effourt.calenkit.domain.Auth;
import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.dto.AccessTokenResponse;
import com.effourt.calenkit.dto.AuthUserInfoResponse;
import com.effourt.calenkit.repository.AuthRepository;
import com.effourt.calenkit.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;

    //[email로 회원가입 행위] : joinByEmail()
    // => EmailSend
    // => MemberRepository.save
    // **** 필수 정보 입력 처리 요망 ****
    public void joinByEmail(Member member) {
        memberRepository.save(member);
    }

    //[sns로 회원가입 행위] : joinBySNS()
    // => MemberRepository.save
    // => AuthRepository.save
    public Member joinBySns(AuthUserInfoResponse authUserInfoResponse, AccessTokenResponse accessTokenResponse) {
        Auth auth = new Auth();
        auth.setAuthRefresh(accessTokenResponse.getRefreshToken());
        auth.setAuthAccess(accessTokenResponse.getAccessToken());
        authRepository.save(auth);

        Member member = new Member();
        member.setMemId(authUserInfoResponse.getEmail());
        member.setMemName(authUserInfoResponse.getNickname());
        member.setMemImage(authUserInfoResponse.getProfileImage());
        member.setMemAuthId(auth.getAuthId());
        memberRepository.save(member);
        return member;
    }
}
