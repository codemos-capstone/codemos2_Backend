package com.seoultech.codemos.service;

import com.seoultech.codemos.dto.EmailDTO;
import com.seoultech.codemos.jwt.TokenProvider;
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
    private final TokenProvider tokenProvider;

    @Value("${spring.mail.username}")
    private String from;
    public void sendMail(String email) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage(); // MimeMessage 객체 생성
        try {
            String token = tokenProvider.createResetPasswordToken(email);
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Password Reset Request");

            // 비밀번호 재설정 링크를 포함한 메시지 생성
            String resetPasswordUrl = "http://localhost:3000/auth/reset-password?token=" + token;
            String messageContent =  "<h2>비밀번호 변경을 위해, 아래 링크를 클릭하세요</h2>"
                    + "<p>To reset your password, please click the link below</p>"
                    + "<a href=\"" + resetPasswordUrl + "\">비밀번호 재설정</a>";

            mimeMessageHelper.setText(messageContent, true);
            mimeMessageHelper.setFrom(new InternetAddress(from));

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw  new RuntimeException(e);
        }
    }
}