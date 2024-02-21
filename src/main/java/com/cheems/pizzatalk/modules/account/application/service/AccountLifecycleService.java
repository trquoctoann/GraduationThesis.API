package com.cheems.pizzatalk.modules.account.application.service;

import com.cheems.pizzatalk.common.filter.StringFilter;
import com.cheems.pizzatalk.entities.enumeration.UserStatus;
import com.cheems.pizzatalk.entities.filter.UserStatusFilter;
import com.cheems.pizzatalk.modules.account.application.port.in.share.AccountLifecycleUseCase;
import com.cheems.pizzatalk.modules.user.application.port.in.command.ResetPasswordUserPasswordCommand;
import com.cheems.pizzatalk.modules.user.application.port.in.query.UserCriteria;
import com.cheems.pizzatalk.modules.user.application.port.in.share.QueryUserUseCase;
import com.cheems.pizzatalk.modules.user.application.port.out.UserPort;
import com.cheems.pizzatalk.modules.user.domain.User;
import java.time.Instant;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountLifecycleService implements AccountLifecycleUseCase {

    private static final Logger log = LoggerFactory.getLogger(AccountLifecycleService.class);

    private final UserPort userPort;

    private final PasswordEncoder passwordEncoder;

    private final QueryUserUseCase queryUserUseCase;

    public AccountLifecycleService(UserPort userPort, PasswordEncoder passwordEncoder, QueryUserUseCase queryUserUseCase) {
        this.userPort = userPort;
        this.passwordEncoder = passwordEncoder;
        this.queryUserUseCase = queryUserUseCase;
    }

    @Override
    public Optional<User> activateAccount(String activationKey) {
        log.debug("Activating account for activation key: {}", activationKey);
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
                log.debug("Account {} has been activated", user.getId());
                return user;
            });
    }

    @Override
    public Optional<User> requestResetPassword(String email) {
        log.debug("Requesting reset password for account has email: {}", email);
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
                log.debug("Reset key has been created for account {}", user.getId());
                return user;
            });
    }

    @Override
    public Optional<User> resetPassword(ResetPasswordUserPasswordCommand command) {
        String resetKey = command.getKey();
        String newPassword = command.getNewPassword();

        log.debug("Reset account password for reset key {}", resetKey);
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
                log.debug("Reset password successfully for account {}", user.getId());
                return user;
            });
    }
}
