package com.effourt.calenkit.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailMessage {

    private String recipient;
    private String subject;
    private String message;
}
