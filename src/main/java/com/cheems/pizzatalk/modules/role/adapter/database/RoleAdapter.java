package com.cheems.pizzatalk.modules.role.adapter.database;

import com.cheems.pizzatalk.entities.RolePermissionEntity;
import com.cheems.pizzatalk.entities.mapper.PermissionMapper;
import com.cheems.pizzatalk.entities.mapper.RoleMapper;
import com.cheems.pizzatalk.modules.role.application.port.out.RolePort;
import com.cheems.pizzatalk.modules.role.domain.Role;
import com.cheems.pizzatalk.repository.RolePermissionRepository;
import com.cheems.pizzatalk.repository.RoleRepository;
import com.cheems.pizzatalk.repository.UserRoleRepository;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class RoleAdapter implements RolePort {

    private final RoleRepository roleRepository;

    private final UserRoleRepository userRoleRepository;

    private final RolePermissionRepository rolePermissionRepository;

    private final RoleMapper roleMapper;

    private final PermissionMapper permissionMapper;

    public RoleAdapter(
        RoleRepository roleRepository,
        UserRoleRepository userRoleRepository,
        RolePermissionRepository rolePermissionRepository,
        RoleMapper roleMapper,
        PermissionMapper permissionMapper
    ) {
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.roleMapper = roleMapper;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public Role save(Role role) {
        return roleMapper.toDomain(roleRepository.save(roleMapper.toEntity(role)));
    }

    @Override
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public void savePermissionToRole(Long roleId, Set<Long> permissionIds) {
        permissionIds.forEach(permissionId -> {
            RolePermissionEntity rolePermissionEntity = new RolePermissionEntity();
            rolePermissionEntity.setRole(roleMapper.fromId(roleId));
            rolePermissionEntity.setPermission(permissionMapper.fromId(permissionId));
            rolePermissionRepository.save(rolePermissionEntity);
        });
    }

    @Override
    public void removePermissionOfRole(Long roleId, Set<Long> permissionIds) {
        Set<RolePermissionEntity> rolePermissionEntities = rolePermissionRepository.findByRoleIdAndPermissionIds(roleId, permissionIds);
        rolePermissionRepository.deleteAll(rolePermissionEntities);
    }

    @Override
    public void removeAllPermissionOfRole(Long roleId) {
        rolePermissionRepository.deleteByRoleId(roleId);
    }

    @Override
    public void removeRoleForAllUsers(Long roleId) {
        userRoleRepository.deleteByRoleId(roleId);
    }
}
