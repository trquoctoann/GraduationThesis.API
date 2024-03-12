package com.cheems.pizzatalk.entities.mapper;

import com.cheems.pizzatalk.common.mapper.EntityMapper;
import com.cheems.pizzatalk.entities.UserKeyEntity;
import com.cheems.pizzatalk.modules.userkey.domain.UserKey;
import java.util.HashSet;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface UserKeyMapper extends EntityMapper<UserKey, UserKeyEntity> {
    String DOMAIN_USER = "user";

    String ENTITY_USER = "user";

    @Override
    @Mapping(target = "user", source = "userId")
    UserKeyEntity toEntity(UserKey domain);

    @Override
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "user", ignore = true)
    UserKey toDomain(UserKeyEntity entity);

    // prettier-ignore
    default Set<String> toEntityAttributes(Set<String> domainAttributes) {
        Set<String> entityAttributes = new HashSet<>();
        domainAttributes.forEach(
            domainAttribute -> {
                if (domainAttribute.equals(DOMAIN_USER)) {
                    entityAttributes.add(ENTITY_USER);
                }
            }
        );
        return entityAttributes;
    }

    default UserKeyEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserKeyEntity userKeyEntity = new UserKeyEntity();
        userKeyEntity.setId(id);
        return userKeyEntity;
    }
}
