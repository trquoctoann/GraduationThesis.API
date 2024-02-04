package com.cheems.pizzatalk.modules.user.application.port.in.share;

import java.util.Set;

public interface UserRoleUseCase {
    void saveRoleToUser(String username, Set<Long> roleIds);

    void saveRoleToUser(Long userId, Set<Long> roleIds);
}
