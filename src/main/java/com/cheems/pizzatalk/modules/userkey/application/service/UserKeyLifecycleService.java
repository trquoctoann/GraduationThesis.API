package com.cheems.pizzatalk.modules.userkey.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.entities.enumeration.UserKeyStatus;
import com.cheems.pizzatalk.entities.enumeration.UserKeyType;
import com.cheems.pizzatalk.entities.enumeration.UserStatus;
import com.cheems.pizzatalk.modules.user.application.port.in.share.QueryUserUseCase;
import com.cheems.pizzatalk.modules.user.domain.User;
import com.cheems.pizzatalk.modules.userkey.application.port.in.share.QueryUserKeyUseCase;
import com.cheems.pizzatalk.modules.userkey.application.port.in.share.UserKeyLifecycleUseCase;
import com.cheems.pizzatalk.modules.userkey.application.port.out.UserKeyPort;
import com.cheems.pizzatalk.modules.userkey.domain.UserKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserKeyLifecycleService implements UserKeyLifecycleUseCase {

    private static final Logger log = LoggerFactory.getLogger(UserKeyLifecycleService.class);

    private final UserKeyPort userKeyPort;

    private final QueryUserKeyUseCase queryUserKeyUseCase;

    private final QueryUserUseCase queryUserUseCase;

    public UserKeyLifecycleService(UserKeyPort userKeyPort, QueryUserKeyUseCase queryUserKeyUseCase, QueryUserUseCase queryUserUseCase) {
        this.userKeyPort = userKeyPort;
        this.queryUserKeyUseCase = queryUserKeyUseCase;
        this.queryUserUseCase = queryUserUseCase;
    }

    @Override
    public UserKey create(UserKeyType type, Long userId) {
        log.debug("Creating {} key for user, id: {}", type, userId);
        List<UserKey> existUserKeys = queryUserKeyUseCase.findActiveKeyByUserId(type, userId);
        if (existUserKeys.size() >= 1) {
            throw new BusinessException(type + " key already exists for user ID: " + userId + " and currently active");
        }

        User userRequestCreateKey = queryUserUseCase.getById(userId);
        if (userRequestCreateKey.getStatus().equals(UserStatus.DELETED)) {
            throw new BusinessException("User has been deleted");
        }

        String keyValue;
        do {
            keyValue = UUID.randomUUID().toString();
        } while (queryUserKeyUseCase.findByValue(keyValue).isPresent());

        UserKey userKey = new UserKey();
        userKey.setValue(keyValue);
        userKey.setType(type);
        userKey.setStatus(UserKeyStatus.ACTIVE);
        userKey.setCreationDate(Instant.now());
        userKey.setExpirationDate(Instant.now().plus(1, ChronoUnit.DAYS));
        userKey.setUserId(userId);

        userKey = userKeyPort.save(userKey);
        log.debug("Created {} key for user, id: {}", type, userId);
        return userKey;
    }

    @Override
    public UserKey updateStatus(String value, UserKeyStatus newStatus) {
        log.debug("Updating status for user key has value: {} to {}", value, newStatus);
        UserKey existUserKey = queryUserKeyUseCase.getByValue(value);

        UserKeyStatus currentStatus = existUserKey.getStatus();
        if (currentStatus.equals(UserKeyStatus.EXPIRED) || existUserKey.getExpirationDate().isBefore(Instant.now())) {
            throw new BusinessException("Provided key has been expired");
        } else if (currentStatus.equals(UserKeyStatus.USED)) {
            throw new BusinessException("Provided key has been used");
        }

        if (newStatus.equals(UserKeyStatus.USED)) {
            existUserKey.setUsedDate(Instant.now());
        }

        existUserKey.setStatus(newStatus);
        existUserKey = userKeyPort.save(existUserKey);

        log.debug("Updated status for user key has value: {} to {}", value, newStatus);
        return existUserKey;
    }
}
