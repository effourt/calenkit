package com.effourt.calenkit.exception;

import com.effourt.calenkit.domain.Team;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ExistsTeamException extends Exception {
    private static final long serialVersionUID = 1L;

    private Team team;
    public ExistsTeamException() {
        // TODO Auto-generated constructor stub
    }

    public ExistsTeamException(String message, Team team) {
        super(message);
        this.team=team;
    }
}