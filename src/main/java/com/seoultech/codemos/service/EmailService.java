package com.seoultech.codemos.service;

import com.seoultech.codemos.dto.EmailDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(EmailDTO emailMessage) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage(); // MimeMessage 객체 생성
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            mimeMessageHelper.setTo(emailMessage.getTo());

            mimeMessageHelper.setSubject(emailMessage.getSubject());

            mimeMessageHelper.setText(emailMessage.getMessage(), false);

            mimeMessageHelper.setFrom(new InternetAddress(from));

            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw  new RuntimeException(e);
        }
    }
}