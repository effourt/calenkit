package com.effourt.calenkit.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthUserInfoResponse {
    private Long id; //회원번호
    private String email; //카카오 계정 이메일
    private String nickname; //카카오 계정 닉네임
    private String profileImage; //카카오 계정 프로필 이미지
}
