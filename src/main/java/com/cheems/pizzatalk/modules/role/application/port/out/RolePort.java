package com.cheems.pizzatalk.modules.role.application.port.out;

import com.cheems.pizzatalk.modules.role.domain.Role;

public interface RolePort {
    Role save(Role role);

    void deleteById(Long id);

    void removeRoleOfUserByRoleId(Long id);
}
