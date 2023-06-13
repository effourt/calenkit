package com.effourt.calenkit.domain;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class Member {
    private Integer memAuthId;
    @NotNull
    @Pattern(regexp = "[a-z0-9]+@[a-z]+\\.[a-z]{2,3}", message = "이메일이 올바르지 않습니다.")
    private String memId;
    private String memImage;
    private String memLogin;
    @NotNull
    @Pattern(regexp = "[a-zA-Z가-힣]{2,10}", message = "2자리 이상 10자리 이하의 영문, 한글을 사용하세요.")
    private String memName;
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#])[\\da-zA-Z!@#]{8,15}", message = "8자리 이상 15자리 이하의 영문, 숫자, 특수문자를 사용하세요.")
    private String memPw;
    private Integer memStatus;
}