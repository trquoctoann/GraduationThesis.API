package com.cheems.pizzatalk.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
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

    private final ResourceLoader resourceLoader;

    public MailService(JavaMailSender javaMailSender, ResourceLoader resourceLoader) {
        this.javaMailSender = javaMailSender;
        this.resourceLoader = resourceLoader;
    }

    @Async
    public void sendEmail(String to, String subject, String content, Boolean isHtml) {
        log.debug("Send email to {}, with subject: {} and content: {}", to, subject, content);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(this.sourceMail);
            message.setSubject(subject);
            message.setText(content, isHtml);

            Resource logo = resourceLoader.getResource("classpath:logo.png");
            message.addInline("logo", logo);

            javaMailSender.send(mimeMessage);
            log.debug("Sent email to {}", to);
        } catch (MailException | MessagingException e) {
            log.warn("Email could not be send to {}", to, e);
        }
    }

    public String loadHtmlContent(String resourcePath) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + resourcePath);
        return new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);
    }
}
