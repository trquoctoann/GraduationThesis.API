package com.cheems.pizzatalk.modules.account.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.entities.enumeration.UserKeyStatus;
import com.cheems.pizzatalk.entities.enumeration.UserKeyType;
import com.cheems.pizzatalk.entities.enumeration.UserStatus;
import com.cheems.pizzatalk.modules.account.application.port.in.command.AccountResetPasswordCommand;
import com.cheems.pizzatalk.modules.account.application.port.in.share.AccountLifecycleUseCase;
import com.cheems.pizzatalk.modules.user.application.port.in.share.QueryUserUseCase;
import com.cheems.pizzatalk.modules.user.application.port.out.UserPort;
import com.cheems.pizzatalk.modules.user.domain.User;
import com.cheems.pizzatalk.modules.userkey.application.port.in.share.QueryUserKeyUseCase;
import com.cheems.pizzatalk.modules.userkey.application.port.in.share.UserKeyLifecycleUseCase;
import com.cheems.pizzatalk.modules.userkey.domain.UserKey;
import com.cheems.pizzatalk.service.MailService;
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

    private final QueryUserKeyUseCase queryUserKeyUseCase;

    private final UserKeyLifecycleUseCase userKeyLifecycleUseCase;

    private final MailService mailService;

    public AccountLifecycleService(
        UserPort userPort,
        PasswordEncoder passwordEncoder,
        QueryUserUseCase queryUserUseCase,
        QueryUserKeyUseCase queryUserKeyUseCase,
        UserKeyLifecycleUseCase userKeyLifecycleUseCase,
        MailService mailService
    ) {
        this.userPort = userPort;
        this.passwordEncoder = passwordEncoder;
        this.queryUserUseCase = queryUserUseCase;
        this.queryUserKeyUseCase = queryUserKeyUseCase;
        this.userKeyLifecycleUseCase = userKeyLifecycleUseCase;
        this.mailService = mailService;
    }

    @Override
    public User activateAccount(String activationKey) {
        log.debug("Activating account for activation key: {}", activationKey);
        User userOwnKey = getUserByKey(activationKey);
        if (userOwnKey.getStatus().equals(UserStatus.ACTIVATED)) {
            throw new BusinessException("User is currently activated");
        }
        userOwnKey.setStatus(UserStatus.ACTIVATED);
        userOwnKey = userPort.save(userOwnKey);

        userKeyLifecycleUseCase.updateStatus(activationKey, UserKeyStatus.USED);

        log.debug("Account {} has been activated", userOwnKey.getId());
        return userOwnKey;
    }

    @Override
    public User requestResetPassword(String email) {
        log.debug("Requesting reset password for account has email: {}", email);
        User user = queryUserUseCase.getByEmail(email);
        userKeyLifecycleUseCase.create(UserKeyType.RESET_KEY, user.getId());

        mailService.sendResetPasswordMailToUser(email);
        log.debug("Reset key has been created for account {}", user.getId());
        return user;
    }

    @Override
    public User resetPassword(String resetKey, AccountResetPasswordCommand command) {
        log.debug("Reseting password using reset key: {}", resetKey);
        User userOwnKey = getUserByKey(resetKey);
        userOwnKey.setPassword(passwordEncoder.encode(command.getNewPassword()));
        userOwnKey = userPort.save(userOwnKey);

        userKeyLifecycleUseCase.updateStatus(resetKey, UserKeyStatus.USED);

        log.debug("Reset password sucessfully for account, ID: {}", userOwnKey.getId());
        return userOwnKey;
    }

    private User getUserByKey(String key) {
        UserKey userKey;
        try {
            userKey = queryUserKeyUseCase.getByValue(key);
        } catch (BusinessException e) {
            throw new BusinessException("Provided key is not exist");
        }

        User userOwnKey = queryUserUseCase.getById(userKey.getUserId());
        return userOwnKey;
    }
}
