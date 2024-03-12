package com.cheems.pizzatalk.modules.userkey.application.port.out;

import com.cheems.pizzatalk.modules.userkey.domain.UserKey;

public interface UserKeyPort {
    UserKey save(UserKey userKey);

    void deleteById(Long id);
}
