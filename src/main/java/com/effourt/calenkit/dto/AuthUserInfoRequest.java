package com.effourt.calenkit.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthUserInfoRequest {
    private String authorization; //(필수)사용자 인증 수단, 액세스 토큰 값 - Authorization: Bearer ${ACCESS_TOKEN}
    private Boolean secureResource; //이미지 URL 값 HTTPS 여부, true 설정 시 HTTPS 사용, 기본 값 false
    private String[] propertyKeys; // Property 키 목록, JSON Array를 ["kakao_account.email"]과 같은 형식으로 사용
}
