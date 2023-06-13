package com.effourt.calenkit.dto;

import lombok.Builder;
import lombok.Getter;

//소셜 로그인
//Access Token 발급을 위한 정보 저장
@Getter
@Builder
public class AccessTokenRequest {
    private String clientId; //앱 REST API 키
    private String clientSecret; //토큰 발급 시, 보안을 강화하기 위해 추가 확인하는 코드
    private String grantType; //authorization_code로 고정
    private String redirectUri; //인가 코드가 리다이렉트된 URI
    private String code; //인가 코드 받기 요청으로 얻은 인가 코드
}
