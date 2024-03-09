package com.cheems.pizzatalk.modules.role.application.port.out;

import com.cheems.pizzatalk.modules.role.domain.Role;
import java.util.Set;

public interface RolePort {
    Role save(Role role);

    void deleteById(Long id);

    void savePermissionToRole(Long roleId, Set<Long> permissionIds);

    void removePermissionOfRole(Long roleId, Set<Long> permissionIds);

    void removeAllPermissionOfRole(Long roleId);

    void removeRoleForAllUsers(Long roleId);
}
