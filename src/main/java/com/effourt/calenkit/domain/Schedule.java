package com.effourt.calenkit.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Schedule {
    private Integer scNo;
    private String scTitle;
    private String scContent;
    private String scSdate;
    private String scEdate;
    private Integer scStatus;
    private Integer scProgress;
}
