package com.effourt.calenkit.dto;

import com.effourt.calenkit.domain.Team;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamShare {
    private Integer teamNo;
    private String teamMid;
    private Integer teamSno;
    private Integer teamLevel;
    private Integer teamBookmark;
    private String image;
}
