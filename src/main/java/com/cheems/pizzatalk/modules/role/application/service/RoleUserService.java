package com.cheems.pizzatalk.modules.role.application.service;

import com.cheems.pizzatalk.modules.role.application.port.in.share.RoleUserUseCase;
import com.cheems.pizzatalk.modules.role.application.port.out.RolePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleUserService implements RoleUserUseCase {

    private static final Logger logger = LoggerFactory.getLogger(RoleUserService.class);

    private final RolePort rolePort;

    public RoleUserService(RolePort rolePort) {
        this.rolePort = rolePort;
    }

    @Override
    public void removeRoleForAllUsers(Long roleId) {
        logger.debug("Removing role, id {} for all users", roleId);
        rolePort.removeRoleForAllUsers(roleId);
        logger.debug("Removed role, id {} for all users", roleId);
    }
}
