package com.cheems.pizzatalk.entities.mapper;

import com.cheems.pizzatalk.common.mapper.EntityMapper;
import com.cheems.pizzatalk.entities.UserEntity;
import com.cheems.pizzatalk.modules.user.domain.User;
import java.util.HashSet;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<User, UserEntity> {
    String DOMAIN_ROLE = "roles";

    String ENTITY_ROLE = "userRoles.role";

    // prettier-ignore
    default Set<String> toEntityAttributes(Set<String> domainAttributes) {
        Set<String> entityAttributes = new HashSet<>();
        domainAttributes.forEach(
            domainAttribute -> {
                if (domainAttribute.equals(DOMAIN_ROLE)) {
                    entityAttributes.add(ENTITY_ROLE);
                }
            });
        return entityAttributes;
    }

    default UserEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        return userEntity;
    }
}
