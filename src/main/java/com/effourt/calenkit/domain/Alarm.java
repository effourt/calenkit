package com.effourt.calenkit.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Alarm {
    private Integer alNo;
    private String alScno;
    private String alMid;
    private Integer alStatus;
    private String alTime;
    private Integer alCate;
}
