package com.effourt.calenkit.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {
    private Integer memAuthId;
    private String memId;
    private String memImage;
    private String memLogin;
    private String memName;
    private String memPw;
    private Integer memStatus;
}