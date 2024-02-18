package com.cheems.pizzatalk.service;

import java.nio.charset.StandardCharsets;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    @Value("${spring.mail.username}")
    private String sourceMail;

    private final JavaMailSender javaMailSender;

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(String to, String subject, String content) {
        log.debug("Send email to {}, with subject: {} and content: {}", to, subject, content);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(this.sourceMail);
            message.setSubject(subject);
            message.setText(content);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to {}", to);
        } catch (MailException | MessagingException e) {
            log.warn("Email could not be send to {}", to, e);
        }
    }
}
