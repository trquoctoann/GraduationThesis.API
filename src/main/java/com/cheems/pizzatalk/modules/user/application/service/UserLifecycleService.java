package com.cheems.pizzatalk.modules.user.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.entities.enumeration.UserStatus;
import com.cheems.pizzatalk.modules.user.application.port.in.command.CreateUserCommand;
import com.cheems.pizzatalk.modules.user.application.port.in.command.UpdateUserCommand;
import com.cheems.pizzatalk.modules.user.application.port.in.share.QueryUserUseCase;
import com.cheems.pizzatalk.modules.user.application.port.in.share.UserLifecycleUseCase;
import com.cheems.pizzatalk.modules.user.application.port.out.UserPort;
import com.cheems.pizzatalk.modules.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserLifecycleService implements UserLifecycleUseCase {

    private static final Logger log = LoggerFactory.getLogger(UserLifecycleService.class);

    private final ObjectMapper objectMapper;

    private final UserPort userPort;

    private final QueryUserUseCase queryUserUseCase;

    public UserLifecycleService(ObjectMapper objectMapper, UserPort userPort, QueryUserUseCase queryUserUseCase) {
        this.objectMapper = objectMapper;
        this.userPort = userPort;
        this.queryUserUseCase = queryUserUseCase;
    }

    @Override
    public User create(CreateUserCommand command) {
        log.debug("Creating user: {}", command);
        Optional<User> existUser = queryUserUseCase.findByUsername(command.getUsername());
        if (existUser.isPresent()) {
            throw new BusinessException("Username already exists");
        }

        User user = objectMapper.convertValue(command, User.class);
        user.setRawPassword(command.getRawPassword());
        setUserDefaultValue(user);

        user = userPort.save(user);
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
        user.setStatus(existUser.getStatus());

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

        if (user.getLangKey() == null || user.getLangKey() == "") {
            user.setLangKey("en");
        }
    }
}
