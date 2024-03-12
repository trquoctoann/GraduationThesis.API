package com.cheems.pizzatalk.service;

import com.cheems.pizzatalk.entities.enumeration.UserKeyType;
import com.cheems.pizzatalk.modules.user.application.port.in.share.QueryUserUseCase;
import com.cheems.pizzatalk.modules.user.domain.User;
import com.cheems.pizzatalk.modules.userkey.application.port.in.share.QueryUserKeyUseCase;
import com.cheems.pizzatalk.modules.userkey.domain.UserKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
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

    @Value("${spring.application.base-url}")
    private String baseUrl;

    @Value("${spring.mail.username}")
    private String sourceMail;

    private final JavaMailSender javaMailSender;

    private final ResourceLoader resourceLoader;

    private final QueryUserUseCase queryUserUseCase;

    private final QueryUserKeyUseCase queryUserKeyUseCase;

    public MailService(
        JavaMailSender javaMailSender,
        ResourceLoader resourceLoader,
        QueryUserUseCase queryUserUseCase,
        QueryUserKeyUseCase queryUserKeyUseCase
    ) {
        this.javaMailSender = javaMailSender;
        this.resourceLoader = resourceLoader;
        this.queryUserUseCase = queryUserUseCase;
        this.queryUserKeyUseCase = queryUserKeyUseCase;
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

    public void sendActivationMailToUser(String email) {
        User user = queryUserUseCase.getByEmail(email);
        List<UserKey> userKey = queryUserKeyUseCase.findActiveKeyByUserId(UserKeyType.ACTIVATION_KEY, user.getId());

        String activationUrl = this.baseUrl + "/api/accounts/activate?activationKey=" + userKey.get(0).getValue();
        try {
            String htmlContent = loadHtmlContent("html/ActivationEmail.html");
            htmlContent = htmlContent.replace("{{CUSTOMER_FIRSTNAME}}", user.getFirstName());
            htmlContent = htmlContent.replace("{{ACTIVATION_LINK}}", activationUrl);
            sendEmail(email, "Welcome to PizzaTalk!", htmlContent, true);
        } catch (IOException e) {
            log.error("Failed to load email template", e);
        }
    }

    public void sendResetPasswordMailToUser(String email) {
        User user = queryUserUseCase.getByEmail(email);
        List<UserKey> userKey = queryUserKeyUseCase.findActiveKeyByUserId(UserKeyType.RESET_KEY, user.getId());

        String activationUrl = this.baseUrl + "/api/accounts/reset-password?resetKey=" + userKey.get(0).getValue();
        try {
            String htmlContent = loadHtmlContent("html/ResetPasswordEmail.html");
            htmlContent = htmlContent.replace("{{CUSTOMER_FIRSTNAME}}", user.getFirstName());
            htmlContent = htmlContent.replace("{{PASSWORD_RESET_LINK}}", activationUrl);
            sendEmail(email, "Password Reset for PizzaTalk", htmlContent, true);
        } catch (IOException e) {
            log.error("Failed to load email template", e);
        }
    }
}
