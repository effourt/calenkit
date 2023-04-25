package com.effourt.calenkit.service;

import com.effourt.calenkit.client.KakaoFeignClient;
import com.effourt.calenkit.domain.Auth;
import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.dto.AuthUserInfoResponse;
import com.effourt.calenkit.dto.EmailMessage;
import com.effourt.calenkit.dto.AccessTokenRequest;
import com.effourt.calenkit.dto.AccessTokenResponse;
import com.effourt.calenkit.repository.AuthRepository;
import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.util.EmailSend;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;
    private final EmailSend emailSend;
    private final KakaoFeignClient kakaoFeignClient;

    //Member 테이블에 회원 정보 저장
    public void saveMember(Member member) {
        memberRepository.save(member);
    }

    //Member 테이블에서 이메일에 해당하는 회원 정보 조회
    public Member getMemberById(String id) {
        return memberRepository.findByMemId(id);
    }

    /**[email로 로그인 행위] : loginByEmail() */
    // => MemberRepository.find
    // => EmailSend
    // => 세션 값 저장

    // [이메일 로그인] 아이디/비밀번호 존재 여부 확인
    public String checkMember(String memberId) {
        Member member = memberRepository.findByMemId(memberId);
        String loginType = "";
        if (member != null) {
            if (member.getMemPw() != null) {
                //아이디 존재 O, 비밀번호 존재 O
                loginType = "PASSWORD_LOGIN";
            } else {
                //아이디 존재 O, 비밀번호 존재 X
                //로그인 코드 생성 및 메일 전송
                loginType = "CODE_LOGIN";
            }
        } else {
            //아이디 존재 X, 비밀번호 존재 X
            //회원가입 코드 생성 및 메일 전송
            loginType = "JOIN_LOGIN";
        }

        return loginType;
    }

    // 로그인 or 회원가입 코드를 생성하고 세션에 저장
    // 세션키를 쿠키값으로 하는 쿠키를 생성하고 쿠키명을 클라이언트의 메일로 전송
    // 세션에 저장된 ACCESS KEY : 아이디(이메일) + ACCESS
    public void sendCode(String id, EmailMessage emailMessage, HttpSession session) {
        //로그인/회원가입 코드 생성
        emailSend.createAccessCode(id, session);

        //메일 전송
        emailSend.sendMail(emailMessage);
    }

    //인가 코드로 Access 토큰 발급
    public AccessTokenResponse getAccessToken(AccessTokenRequest accessTokenRequest) {
        AccessTokenResponse accessToken = kakaoFeignClient.getAccessToken(
                accessTokenRequest.getClientId(),
                "authorization_code",
                accessTokenRequest.getRedirectUri(),
                accessTokenRequest.getCode());
        log.info("accessToken={}", accessToken.getAccessToken());
        log.info("refreshToken={}", accessToken.getRefreshToken());
        return accessToken;
    }


    //인가 코드로 Access 토큰을 받아온 뒤, Access 토큰으로 카카오 리소스 서버에서 유저 정보 가져오기
    public AuthUserInfoResponse getAuthUserInfo(String accessToken) {
        return kakaoFeignClient.getAuthUserInfo("Bearer " + accessToken);
    }

    //Access 토큰과 Refresh 토큰 저장
    public Auth saveToken(AccessTokenResponse accessToken) {
        Auth auth = new Auth();
        auth.setAuthAccess(accessToken.getAccessToken());
        auth.setAuthRefresh(accessToken.getRefreshToken());
        authRepository.save(auth);
        return auth;
    }

    //Access 토큰과 Refresh 토큰 UPDATE
    public void updateToken(String memberId, Integer authId) {
        Member member = new Member();
        member.setMemId(memberId);
        member.setMemAuthId(authId);
        memberRepository.update(member);
    }

}
