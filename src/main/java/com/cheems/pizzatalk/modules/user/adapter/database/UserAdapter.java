package com.cheems.pizzatalk.modules.user.adapter.database;

import com.cheems.pizzatalk.entities.UserRoleEntity;
import com.cheems.pizzatalk.entities.mapper.RoleMapper;
import com.cheems.pizzatalk.entities.mapper.UserMapper;
import com.cheems.pizzatalk.modules.user.application.port.out.UserPort;
import com.cheems.pizzatalk.modules.user.domain.User;
import com.cheems.pizzatalk.repository.UserRepository;
import com.cheems.pizzatalk.repository.UserRoleRepository;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class UserAdapter implements UserPort {

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private final UserMapper userMapper;

    private final RoleMapper roleMapper;

    public UserAdapter(UserRepository userRepository, UserRoleRepository userRoleRepository, UserMapper userMapper, RoleMapper roleMapper) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
    }

    @Override
    public User save(User user) {
        return userMapper.toDomain(userRepository.save(userMapper.toEntity(user)));
    }

    @Override
    public void saveRole(Long userId, Set<Long> roleIds) {
        roleIds.forEach(roleId -> {
            UserRoleEntity userRoleEntity = new UserRoleEntity();
            userRoleEntity.setRole(roleMapper.fromId(roleId));
            userRoleEntity.setUser(userMapper.fromId(userId));
            userRoleRepository.save(userRoleEntity);
        });
    }

    @Override
    public void removeRole(Long userId, Set<Long> roleIds) {
        Set<UserRoleEntity> existUserRoleEntities = userRoleRepository.findByUserIdAndRoleIds(userId, roleIds);
        userRoleRepository.deleteAll(existUserRoleEntities);
    }

    @Override
    public void removeAllRoleOfUser(Long userId) {
        userRoleRepository.deleteByUserId(userId);
    }
}
