package com.indytskyi.userserviceairport.email.impl;

import com.indytskyi.userserviceairport.email.EmailSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EmailSenderImpl implements EmailSender {

    private JavaMailSender mailSender;
    @Override
    @Async
    public void send(String to, String email) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom("artem234325@gmail.com");
            mailSender.send(message);

        } catch (MessagingException e) {
            log.error("failed ti send email", e);
            throw  new IllegalStateException("failed to send email");
        }
    }
}
