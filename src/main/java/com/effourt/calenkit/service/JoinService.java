package com.effourt.calenkit.service;

import com.effourt.calenkit.domain.Auth;
import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.dto.AccessTokenResponse;
import com.effourt.calenkit.dto.AuthUserInfoResponse;
import com.effourt.calenkit.repository.AuthRepository;
import com.effourt.calenkit.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;

    /**
     * 이메일로 회원가입
     * @param member 회원가입 할 회원 정보
     */
    @Transactional
    public void joinByEmail(Member member) {
        memberRepository.save(member);
    }

    /**
     * 소셜로그인 시 회원가입
     * 액세스 토큰, 리프레시 토큰 저장 및 회원 정보 저장
     * @param authUserInfoResponse 저장할 회원 정보
     * @param accessTokenResponse 저장할 액세스 토큰, 리프레스 토큰 정보
     * @return 저장된 회원 정보
     */
    @Transactional
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
