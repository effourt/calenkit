package com.effourt.calenkit.exception.handler;

import com.effourt.calenkit.controller.TeamController;
import com.effourt.calenkit.exception.ExistsTeamException;
import com.effourt.calenkit.exception.MemberNotFoundException;
import com.effourt.calenkit.exception.ScheduleNotFoundException;
import com.effourt.calenkit.exception.TeamNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = {TeamController.class})
public class TeamExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND) //404
    @ExceptionHandler
    public String TeamNotFoundExceptionHandler(TeamNotFoundException e){
        log.info("[TeamNotFoundException] = {}, {}, {}", e.getMessage(), e.getScNo(), e.getId());
        return e.getMessage();
    }
    @ResponseStatus(HttpStatus.NOT_FOUND) //404
    @ExceptionHandler
    public String ScheduleNotFoundExceptionHandler(ScheduleNotFoundException e){
        log.info("[ScheduleNotFoundException] = {}, {}", e.getMessage(), e.getScNo());
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.CONFLICT) //409
    @ExceptionHandler
    public String ExistsTeamExceptionHandler(ExistsTeamException e){
        log.info("[ExistsTeamException] = {}, {}", e.getMessage(), e.getTeam().getTeamMid());
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND) //404
    @ExceptionHandler
    public String MemberNotFoundExceptionHandler(MemberNotFoundException e){
        log.info("[MemberNotFoundException] = {}", e.getMessage());
        return e.getMessage();
    }
}
