package com.effourt.calenkit.dto;


import com.effourt.calenkit.domain.Schedule;
import com.effourt.calenkit.domain.Team;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MySchedule {
    private Team team;
    private Schedule schedule;
}