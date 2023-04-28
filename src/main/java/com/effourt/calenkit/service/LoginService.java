package com.effourt.calenkit.service;

import com.effourt.calenkit.client.KakaoApiClient;
import com.effourt.calenkit.client.KakaoFeignClient;
import com.effourt.calenkit.domain.Auth;
import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.dto.AccessTokenRequest;
import com.effourt.calenkit.dto.AccessTokenResponse;
import com.effourt.calenkit.dto.AuthUserInfoResponse;
import com.effourt.calenkit.repository.AuthRepository;
import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.util.EmailSend;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;
    private final EmailSend emailSend;
    private final KakaoFeignClient kakaoFeignClient;
    private final KakaoApiClient kakaoApiClient;

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
            } else if (member.getMemPw() == null) {
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

    /**
     * 인가 코드로 Access 토큰 발급
     * @param accessTokenRequest 액세스 토큰 요청을 위한 정보를 담은 객체
     * @return
     */
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


    /**
     * 인가 코드로 Access 토큰을 받아온 뒤, Access 토큰으로 카카오 리소스 서버에서 유저 정보 가져오기
     * @param accessToken 사용자 정보 조회를 위한 액세스 토큰
     * @return
     */
    public AuthUserInfoResponse getAuthUserInfo(String accessToken) {
        String propertyKeys = "[\"id\",\"kakao_account.email\",\"kakao_account.profile.nickname\",\"kakao_account.profile.profile_image_url\"]";
        String userInfoString = kakaoApiClient.getAuthUserInfo("Bearer " + accessToken, propertyKeys);
        AuthUserInfoResponse userInfo = new AuthUserInfoResponse();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = null;
            jsonNode = objectMapper.readTree(userInfoString);
            userInfo.setId(jsonNode.get("id").asLong());
            userInfo.setEmail(jsonNode.get("kakao_account").get("email").asText());
            userInfo.setNickname(jsonNode.get("kakao_account").get("profile").get("nickname").asText());
            userInfo.setProfileImage(jsonNode.get("kakao_account").get("profile").get("profile_image_url").asText());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        log.info("userId={}", userInfo.getId());
        log.info("userEmail={}", userInfo.getEmail());
        log.info("userNickname={}", userInfo.getNickname());
        log.info("userProfileImage={}", userInfo.getProfileImage());
        return userInfo;
    }

    /**
     * Access 토큰과 Refresh 토큰 저장
     * @param token DB에 저장할 토큰 정보를 담은 객체
     * @return 토큰 정보 (Access Token, Refresh Token)
     */
    public Auth saveToken(AccessTokenResponse token) {
        Auth auth = new Auth();
        auth.setAuthAccess(token.getAccessToken());
        auth.setAuthRefresh(token.getRefreshToken());
        authRepository.save(auth);
        return auth;
    }

    /**
     * Access 토큰과 Refresh 토큰 UPDATE
     * @param authId DB에 저장된 토큰 인덱스
     * @param accessTokenResponse 액세스 토큰, 리프레시 토큰 정보를 담은 객체
     */
    public void updateToken(Integer authId, AccessTokenResponse accessTokenResponse) {
        Auth auth = new Auth();
        auth.setAuthId(authId);
        auth.setAuthAccess(accessTokenResponse.getAccessToken());
        auth.setAuthRefresh(accessTokenResponse.getRefreshToken());
        authRepository.update(auth);
    }

    /**
     * Access Token 유효기간 만료
     * @param accessToken OAuth 로그아웃을 위한 액세스 토큰 (Bearer ${accessToken})
     */
    public void expireToken(String accessToken) {
        kakaoApiClient.logout("Bearer " + accessToken);
    }

}
