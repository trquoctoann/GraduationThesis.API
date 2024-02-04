package com.cheems.pizzatalk.modules.user.application.port.out;

import com.cheems.pizzatalk.modules.user.domain.User;
import java.util.Set;

public interface UserPort {
    User save(User user);

    void saveRole(Long userId, Set<Long> roleIds);

    void removeRole(Long userId, Set<Long> roleIds);
}
