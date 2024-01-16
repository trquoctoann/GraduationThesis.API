package com.cheems.pizzatalk.modules.role.application.port.in.share;

import com.cheems.pizzatalk.modules.role.application.port.in.command.CreateRoleCommand;
import com.cheems.pizzatalk.modules.role.application.port.in.command.UpdateRoleCommand;
import com.cheems.pizzatalk.modules.role.domain.Role;

public interface RoleLifecycleUseCase {
    Role create(CreateRoleCommand command);

    Role update(UpdateRoleCommand command);

    void deleteById(Long id);
}
