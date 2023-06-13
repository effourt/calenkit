package com.effourt.calenkit.util;

import com.effourt.calenkit.dto.EmailMessage;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmailSendTest {

    @Autowired
    EmailSend emailSend;

//    @Test
//    void sendMail() {
//        EmailMessage emailMessage = EmailMessage.builder()
//                .recipient("---받는 사람 이메일---")
//                .subject("테스트")
//                .message("메일 전송 테스트")
//                .build();
//
//        emailSend.sendMail(emailMessage);
//    }
}