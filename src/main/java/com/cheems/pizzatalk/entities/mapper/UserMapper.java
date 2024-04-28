package com.cheems.pizzatalk.entities.mapper;

import com.cheems.pizzatalk.common.mapper.EntityMapper;
import com.cheems.pizzatalk.entities.UserEntity;
import com.cheems.pizzatalk.modules.user.domain.User;
import java.util.HashSet;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends EntityMapper<User, UserEntity> {
    String DOMAIN_ROLE = "roles";
    String DOMAIN_PERMISSION = "permissions";
    String DOMAIN_CONVERSATION = "conversations";
    String DOMAIN_CART = "carts";

    String ENTITY_ROLE = "userRoles.role";
    String ENTITY_PERMISSION = "userRoles.role.rolePermissions.permission";
    String ENTITY_CONVERSATION = "participants.conversation";
    String ENTITY_CART = "carts";

    @Override
    @Mapping(target = "carts", ignore = true)
    UserEntity toEntity(User domain);

    @Override
    @Mapping(target = "carts", ignore = true)
    User toDomain(UserEntity entity);

    // prettier-ignore
    @Override
    default Set<String> toEntityAttributes(Set<String> domainAttributes) {
        Set<String> entityAttributes = new HashSet<>();
        domainAttributes.forEach(
            domainAttribute -> {
                if (domainAttribute.equals(DOMAIN_ROLE)) {
                    entityAttributes.add(ENTITY_ROLE);
                }
                if (domainAttribute.equals(DOMAIN_PERMISSION)) {
                    entityAttributes.add(ENTITY_PERMISSION);
                }
                if (domainAttribute.equals(DOMAIN_CONVERSATION)) {
                    entityAttributes.add(ENTITY_CONVERSATION);
                }
                if (domainAttribute.equals(DOMAIN_CART)) {
                    entityAttributes.add(ENTITY_CART);
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
