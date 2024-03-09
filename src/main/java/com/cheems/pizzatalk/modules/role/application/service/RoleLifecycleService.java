package com.cheems.pizzatalk.modules.role.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.modules.role.application.port.in.command.CreateRoleCommand;
import com.cheems.pizzatalk.modules.role.application.port.in.command.UpdateRoleCommand;
import com.cheems.pizzatalk.modules.role.application.port.in.share.QueryRoleUseCase;
import com.cheems.pizzatalk.modules.role.application.port.in.share.RoleLifecycleUseCase;
import com.cheems.pizzatalk.modules.role.application.port.in.share.RolePermissionUseCase;
import com.cheems.pizzatalk.modules.role.application.port.in.share.RoleUserUseCase;
import com.cheems.pizzatalk.modules.role.application.port.out.RolePort;
import com.cheems.pizzatalk.modules.role.domain.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleLifecycleService implements RoleLifecycleUseCase {

    private static final Logger logger = LoggerFactory.getLogger(RoleLifecycleService.class);

    private final ObjectMapper objectMapper;

    private final RolePort rolePort;

    private final RoleUserUseCase roleUserUseCase;

    private final RolePermissionUseCase rolePermissionUseCase;

    private final QueryRoleUseCase queryRoleUseCase;

    public RoleLifecycleService(
        ObjectMapper objectMapper,
        RolePort rolePort,
        RoleUserUseCase roleUserUseCase,
        RolePermissionUseCase rolePermissionUseCase,
        QueryRoleUseCase queryRoleUseCase
    ) {
        this.objectMapper = objectMapper;
        this.rolePort = rolePort;
        this.roleUserUseCase = roleUserUseCase;
        this.rolePermissionUseCase = rolePermissionUseCase;
        this.queryRoleUseCase = queryRoleUseCase;
    }

    @Override
    public Role create(CreateRoleCommand command) {
        logger.debug("Creating role: {}", command);
        validateRole(command);

        Role role = objectMapper.convertValue(command, Role.class);
        role = rolePort.save(role);

        logger.debug("Created role: {}", command);
        return role;
    }

    @Override
    public Role update(UpdateRoleCommand command) {
        logger.debug("Updating role, id: {}", command.getId());
        validateRole(command);
        Role existRole = queryRoleUseCase.getById(command.getId());

        Role role = objectMapper.convertValue(existRole, Role.class);
        role.setId(existRole.getId());
        role = rolePort.save(role);

        logger.debug("Updated role, id: {}", command.getId());
        return role;
    }

    @Override
    public void deleteById(Long roleId) {
        logger.debug("Deleting role, id: {}", roleId);

        rolePermissionUseCase.removeAllPermissionOfRole(roleId);
        roleUserUseCase.removeRoleForAllUsers(roleId);
        rolePort.deleteById(roleId);

        logger.debug("Deleted role, id: {}", roleId);
    }

    private <T extends CreateRoleCommand> void validateRole(T command) {
        if (queryRoleUseCase.findByName(command.getName()).isPresent()) {
            throw new BusinessException("Role already exists");
        }
    }
}
