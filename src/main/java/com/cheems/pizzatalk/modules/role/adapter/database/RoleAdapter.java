package com.cheems.pizzatalk.modules.role.adapter.database;

import com.cheems.pizzatalk.entities.mapper.RoleMapper;
import com.cheems.pizzatalk.modules.role.application.port.out.RolePort;
import com.cheems.pizzatalk.modules.role.domain.Role;
import com.cheems.pizzatalk.repository.RoleRepository;
import com.cheems.pizzatalk.repository.UserRoleRepository;
import org.springframework.stereotype.Component;

@Component
public class RoleAdapter implements RolePort {

    private final RoleRepository roleRepository;

    private final UserRoleRepository userRoleRepository;

    private final RoleMapper roleMapper;

    public RoleAdapter(RoleRepository roleRepository, UserRoleRepository userRoleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleMapper = roleMapper;
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
    public void removeRoleOfUserByRoleId(Long id) {
        userRoleRepository.deleteByRoleId(id);
    }
}
