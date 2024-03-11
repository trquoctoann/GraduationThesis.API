package com.cheems.pizzatalk.modules.user.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.entities.enumeration.UserStatus;
import com.cheems.pizzatalk.modules.user.application.port.in.command.CreateUserCommand;
import com.cheems.pizzatalk.modules.user.application.port.in.command.UpdateUserCommand;
import com.cheems.pizzatalk.modules.user.application.port.in.share.QueryUserUseCase;
import com.cheems.pizzatalk.modules.user.application.port.in.share.UserLifecycleUseCase;
import com.cheems.pizzatalk.modules.user.application.port.in.share.UserRoleUseCase;
import com.cheems.pizzatalk.modules.user.application.port.out.UserPort;
import com.cheems.pizzatalk.modules.user.domain.User;
import com.cheems.pizzatalk.security.RoleConstants;
import com.cheems.pizzatalk.service.MailService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Set;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserLifecycleService implements UserLifecycleUseCase {

    private static final Logger log = LoggerFactory.getLogger(UserLifecycleService.class);

    @Value("${spring.application.base-url}")
    private String baseUrl;

    private final ObjectMapper objectMapper;

    private final UserPort userPort;

    private final PasswordEncoder passwordEncoder;

    private final QueryUserUseCase queryUserUseCase;

    private final UserRoleUseCase userRoleUseCase;

    private final MailService mailService;

    public UserLifecycleService(
        ObjectMapper objectMapper,
        UserPort userPort,
        PasswordEncoder passwordEncoder,
        QueryUserUseCase queryUserUseCase,
        UserRoleUseCase userRoleUseCase,
        MailService mailService
    ) {
        this.objectMapper = objectMapper;
        this.userPort = userPort;
        this.passwordEncoder = passwordEncoder;
        this.queryUserUseCase = queryUserUseCase;
        this.userRoleUseCase = userRoleUseCase;
        this.mailService = mailService;
    }

    @Override
    public User create(CreateUserCommand command) {
        log.debug("Creating user: {}", command);
        if (queryUserUseCase.findByUsername(command.getUsername()).isPresent()) {
            throw new BusinessException("Username already exists");
        } else if (queryUserUseCase.findByEmail(command.getEmail()).isPresent()) {
            throw new BusinessException("Email already exists");
        }

        User user = objectMapper.convertValue(command, User.class);
        setUserDefaultValue(user);

        Set<String> roles = command.getRoleNames();
        roles.removeIf(role -> RoleConstants.ADMINISTRATOR.equals(role));
        roles.add(RoleConstants.USER);

        user = userPort.save(user);
        userRoleUseCase.saveRoleToUser(user.getId(), roles);

        String activationUrl = this.baseUrl + "/api/accounts/activate?activationKey=" + user.getActivationKey();
        try {
            String htmlContent = mailService.loadHtmlContent("html/ActivationEmail.html");
            htmlContent = htmlContent.replace("{{CUSTOMER_FIRSTNAME}}", command.getFirstName());
            htmlContent = htmlContent.replace("{{ACTIVATION_LINK}}", activationUrl);
            mailService.sendEmail(
                command.getEmail(),
                "Welcome to PizzaTalk!",
                htmlContent,
                true
            );
        } catch (IOException e) {
            log.error("Failed to load email template", e);
        }
        log.debug("Created user: {}", command);
        return user;
    }

    @Override
    public User update(UpdateUserCommand command) {
        log.debug("Updating user, id: {}", command.getId());
        User existUser = queryUserUseCase.getById(command.getId());

        User user = objectMapper.convertValue(command, User.class);
        user.setId(existUser.getId());
        user.setUsername(existUser.getUsername());
        user.setEmail(existUser.getEmail());
        user.setActivationKey(existUser.getActivationKey());
        user.setActivationDate(existUser.getActivationDate());
        user.setResetKey(existUser.getResetKey());
        user.setResetDate(existUser.getResetDate());
        user.setStatus(existUser.getStatus());
        user.setRoles(existUser.getRoles());

        user = userPort.save(user);
        log.debug("Updated user, id: {}", command.getId());
        return user;
    }

    @Override
    public void deleteById(Long userId) {
        log.debug("Deleting user, id: {}", userId);
        User user = queryUserUseCase.getById(userId);
        user.setStatus(UserStatus.DELETED);
        userPort.save(user);
        log.debug("Deleted user, id: {}", userId);
    }

    @Override
    public void deleteByUsername(String username) {
        log.debug("Deleting user, username: {}", username);
        User user = queryUserUseCase.getByUsername(username);
        user.setStatus(UserStatus.DELETED);
        userPort.save(user);
        log.debug("Deleted user, id: {}", username);
    }

    private void setUserDefaultValue(User user) {
        user.setStatus(UserStatus.SUSPENDED);
        user.setActivationKey(RandomStringUtils.randomNumeric(20));

        if (user.getLangKey() == null || user.getLangKey() == "") {
            user.setLangKey("vn");
        }

        String encryptedPassword = passwordEncoder.encode(user.getRawPassword());
        user.setPassword(encryptedPassword);
    }
}
