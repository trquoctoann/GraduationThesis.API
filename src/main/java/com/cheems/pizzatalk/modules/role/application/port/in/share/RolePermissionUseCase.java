package com.cheems.pizzatalk.modules.role.application.port.in.share;

import java.util.Set;

public interface RolePermissionUseCase {
    void save(Long roleId, Set<String> permissionNames);

    void removeAllPermissionOfRole(Long roleId);
}
