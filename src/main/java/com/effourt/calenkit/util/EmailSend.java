package com.effourt.calenkit.util;

import com.effourt.calenkit.dto.EmailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSend {

    private final JavaMailSender javaMailSender;

    public void sendMail(EmailMessage emailMessage) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            InternetAddress[] toAddress = new InternetAddress[1];
            toAddress[0] = new InternetAddress(emailMessage.getRecipient());

            mimeMessageHelper.setTo(toAddress);
            mimeMessageHelper.setSubject(emailMessage.getSubject());
            mimeMessageHelper.setText(emailMessage.getMessage());
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Email send error to.'{}'", emailMessage.getRecipient(), e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    //로그인/회원가입 코드 생성
    //세션명 : loginCode
    //세션 Value : ${id}ACCESS
    public String createAccessCode(String id, HttpSession session) {
        String accessCode = UUID.randomUUID().toString();
        session.setAttribute(accessCode, id + "ACCESS");
        session.setMaxInactiveInterval(300);
        return accessCode;
    }

}
