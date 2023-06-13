package com.effourt.calenkit.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Auth {
    private Integer authId;
    private String authRefresh;
    private String authAccess;
}
