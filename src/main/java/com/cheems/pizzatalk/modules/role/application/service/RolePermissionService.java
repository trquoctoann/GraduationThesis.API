package com.cheems.pizzatalk.modules.role.application.service;

import com.cheems.pizzatalk.modules.permission.application.port.in.share.QueryPermissionUseCase;
import com.cheems.pizzatalk.modules.permission.domain.Permission;
import com.cheems.pizzatalk.modules.role.application.port.in.share.RolePermissionUseCase;
import com.cheems.pizzatalk.modules.role.application.port.out.RolePort;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RolePermissionService implements RolePermissionUseCase {

    private static final Logger log = LoggerFactory.getLogger(RolePermissionService.class);

    private final RolePort rolePort;

    private final QueryPermissionUseCase queryPermissionUseCase;

    public RolePermissionService(RolePort rolePort, QueryPermissionUseCase queryPermissionUseCase) {
        this.rolePort = rolePort;
        this.queryPermissionUseCase = queryPermissionUseCase;
    }

    @Override
    public void save(Long roleId, Set<String> permissionNames) {
        log.debug("Saving permissions: {}, to role id: {}", permissionNames, roleId);
        List<Permission> permissions = queryPermissionUseCase.findListByListPermissionNames(new ArrayList<String>(permissionNames));

        Set<Long> requestSavePermissionIds = permissions.stream().map(Permission::getId).collect(Collectors.toSet());
        Set<Long> existsPermissionIds = queryPermissionUseCase
            .findListByRoleId(roleId)
            .stream()
            .map(Permission::getId)
            .collect(Collectors.toSet());

        Set<Long> inSaveDemandPermissionIds = new HashSet<>(requestSavePermissionIds);
        inSaveDemandPermissionIds.removeAll(existsPermissionIds);

        Set<Long> inRemoveDemandPermissionIds = new HashSet<>(existsPermissionIds);
        inRemoveDemandPermissionIds.removeAll(requestSavePermissionIds);

        rolePort.removePermissionOfRole(roleId, inRemoveDemandPermissionIds);
        rolePort.savePermissionToRole(roleId, inSaveDemandPermissionIds);

        log.debug("Saved permissions: {}, to role id: {}", permissionNames, roleId);
    }

    @Override
    public void removeAllPermissionOfRole(Long roleId) {
        log.debug("Removing all permission in role id: {}", roleId);
        rolePort.removeAllPermissionOfRole(roleId);
        log.debug("Removed all permission in role id: {}", roleId);
    }
}
