package com.effourt.calenkit.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ScheduleNotFoundException extends Exception{
    private static final long serialVersionUID = 1L;
    private Integer scNo;
    public ScheduleNotFoundException() {
        // TODO Auto-generated constructor stub
    }

    public ScheduleNotFoundException(String message) {
        super(message);
    }

    public ScheduleNotFoundException(String message, Integer scNo) {
        super(message);
        this.scNo=scNo;
    }
}
