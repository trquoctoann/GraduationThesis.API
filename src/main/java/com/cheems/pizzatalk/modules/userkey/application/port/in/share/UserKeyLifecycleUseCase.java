package com.cheems.pizzatalk.modules.userkey.application.port.in.share;

import com.cheems.pizzatalk.entities.enumeration.UserKeyStatus;
import com.cheems.pizzatalk.entities.enumeration.UserKeyType;
import com.cheems.pizzatalk.modules.userkey.domain.UserKey;

public interface UserKeyLifecycleUseCase {
    UserKey create(UserKeyType type, Long userId);

    UserKey updateStatus(String value, UserKeyStatus newStatus);
}
