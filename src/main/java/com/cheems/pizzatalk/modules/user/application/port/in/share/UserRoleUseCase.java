package com.cheems.pizzatalk.modules.user.application.port.in.share;

import java.util.Set;

public interface UserRoleUseCase {
    void saveRoleToUser(Long userId, Set<String> roleNames);

    void removeAllRoleOfUser(Long userId);
}
