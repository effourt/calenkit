package com.effourt.calenkit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccessTokenResponse {

    @JsonProperty("token_type")
    private String tokenType; //토큰 타입 : bearer 고정
    @JsonProperty("access_token")
    private String accessToken; //사용자 액세스 토큰 값
    @JsonProperty("expires_in")
    private Integer expiresIn; //액세스 토큰의 만료 시간(초)
    @JsonProperty("refresh_token")
    private String refreshToken; //사용자 리프레시 토큰 값
    @JsonProperty("refresh_token_expires_in")
    private Integer refreshTokenExpiresIn; //리프레시 토큰 만료 시간(초)
    private String scope; //인증된 사용자의 정보 조회 권한 범위
}
