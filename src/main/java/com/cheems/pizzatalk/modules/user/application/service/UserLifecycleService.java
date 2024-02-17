package com.cheems.pizzatalk.modules.user.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.common.filter.StringFilter;
import com.cheems.pizzatalk.entities.enumeration.UserStatus;
import com.cheems.pizzatalk.entities.filter.UserStatusFilter;
import com.cheems.pizzatalk.modules.role.application.port.in.share.QueryRoleUseCase;
import com.cheems.pizzatalk.modules.role.domain.Role;
import com.cheems.pizzatalk.modules.user.application.port.in.command.CreateUserCommand;
import com.cheems.pizzatalk.modules.user.application.port.in.command.UpdateUserCommand;
import com.cheems.pizzatalk.modules.user.application.port.in.query.UserCriteria;
import com.cheems.pizzatalk.modules.user.application.port.in.share.QueryUserUseCase;
import com.cheems.pizzatalk.modules.user.application.port.in.share.UserLifecycleUseCase;
import com.cheems.pizzatalk.modules.user.application.port.out.UserPort;
import com.cheems.pizzatalk.modules.user.domain.User;
import com.cheems.pizzatalk.security.AuthorityConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserLifecycleService implements UserLifecycleUseCase {

    private static final Logger log = LoggerFactory.getLogger(UserLifecycleService.class);

    private final ObjectMapper objectMapper;

    private final UserPort userPort;

    private final PasswordEncoder passwordEncoder;

    private final QueryRoleUseCase queryRoleUseCase;

    private final QueryUserUseCase queryUserUseCase;

    public UserLifecycleService(
        ObjectMapper objectMapper,
        UserPort userPort,
        PasswordEncoder passwordEncoder,
        QueryRoleUseCase queryRoleUseCase,
        QueryUserUseCase queryUserUseCase
    ) {
        this.objectMapper = objectMapper;
        this.userPort = userPort;
        this.passwordEncoder = passwordEncoder;
        this.queryRoleUseCase = queryRoleUseCase;
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
        setUserDefaultValue(user);
        String encryptedPassword = passwordEncoder.encode(command.getRawPassword());
        user.setPassword(encryptedPassword);
        Set<Role> roles = new HashSet<>();

        if (command.getRoleIds().size() > 0) {
            for (Long roleId : command.getRoleIds()) {
                Role role = queryRoleUseCase.getById(roleId);
                if (!role.getAuthority().equals(AuthorityConstants.ADMINISTRATOR)) {
                    roles.add(role);
                }
            }
        } else {
            roles.add(queryRoleUseCase.getByAuthority(AuthorityConstants.USER));
        }
        user.setRoles(roles);
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

    @Override
    public Optional<User> activateUser(String activationKey) {
        log.debug("Activating user for activation key: {}", activationKey);
        StringFilter activationKeyFilter = new StringFilter();
        activationKeyFilter.setEquals(activationKey);

        UserCriteria criteria = new UserCriteria();
        criteria.setActivationKey(activationKeyFilter);

        return queryUserUseCase
            .findByCriteria(criteria)
            .map(user -> {
                user.setStatus(UserStatus.ACTIVATED);
                user.setActivationKey(null);
                user.setActivationDate(Instant.now());
                user = userPort.save(user);
                log.debug("User {} has been activated", user.getId());
                return user;
            });
    }

    @Override
    public Optional<User> requestResetPassword(String email) {
        log.debug("Requesting reset password for user has email: {}", email);
        StringFilter emailFilter = new StringFilter();
        emailFilter.setEquals(email);

        UserStatusFilter userStatusFilter = new UserStatusFilter();
        userStatusFilter.setEquals(UserStatus.ACTIVATED);

        UserCriteria criteria = new UserCriteria();
        criteria.setEmail(emailFilter);
        criteria.setStatus(userStatusFilter);

        return queryUserUseCase
            .findByCriteria(criteria)
            .map(user -> {
                user.setResetKey(RandomStringUtils.randomNumeric(20));
                user = userPort.save(user);
                log.debug("Reset key has been created for user {}", user.getId());
                return user;
            });
    }

    @Override
    public Optional<User> resetPassword(String newPassword, String resetKey) {
        log.debug("Reset user password for reset key {}", resetKey);
        StringFilter resetKeyFilter = new StringFilter();
        resetKeyFilter.setEquals(resetKey);

        UserCriteria criteria = new UserCriteria();
        criteria.setResetKey(resetKeyFilter);

        return queryUserUseCase
            .findByCriteria(criteria)
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(resetKey);
                user.setResetDate(Instant.now());
                user = userPort.save(user);
                log.debug("Reset password successfully for user {}", user.getId());
                return user;
            });
    }

    private void setUserDefaultValue(User user) {
        user.setStatus(UserStatus.SUSPENDED);
        user.setActivationKey(RandomStringUtils.randomNumeric(20));

        if (user.getLangKey() == null || user.getLangKey() == "") {
            user.setLangKey("vn");
        }
    }
}
