package com.effourt.calenkit.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamNotFoundException extends Exception{
    private static final long serialVersionUID = 1L;
    private Integer scNo;
    private String id;

    public TeamNotFoundException() {
        // TODO Auto-generated constructor stub
    }

    public TeamNotFoundException(String message) {
        super(message);
    }
    public TeamNotFoundException(String message, Integer scNo) {
        super(message);
        this.scNo=scNo;
    }

    public TeamNotFoundException(String message, Integer scNo, String id) {
        super(message);
        this.scNo=scNo;
        this.id=id;
    }
}