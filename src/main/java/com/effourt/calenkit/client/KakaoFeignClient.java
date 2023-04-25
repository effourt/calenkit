package com.effourt.calenkit.client;

import com.effourt.calenkit.config.FeignConfig;
import com.effourt.calenkit.dto.AccessTokenResponse;
import com.effourt.calenkit.dto.AuthUserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakaoAuth", url = "https://kauth.kakao.com", configuration = {FeignConfig.class})
public interface KakaoFeignClient {

    @PostMapping("/oauth/token")
    AccessTokenResponse getAccessToken(
            @RequestParam("client_id") String clientId,
            @RequestParam("grant_type") String grantType,
            @RequestParam("redirect_uri") String redirectUri,
            @RequestParam("code") String authorizationCode
    );

    @PostMapping("/v2/user/me")
    AuthUserInfoResponse getAuthUserInfo(@RequestHeader("Authorization") String authorization);
}
