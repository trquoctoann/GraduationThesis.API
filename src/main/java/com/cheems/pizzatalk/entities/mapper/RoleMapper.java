package com.cheems.pizzatalk.entities.mapper;

import com.cheems.pizzatalk.common.mapper.EntityMapper;
import com.cheems.pizzatalk.entities.RoleEntity;
import com.cheems.pizzatalk.modules.role.domain.Role;
import java.util.HashSet;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper extends EntityMapper<Role, RoleEntity> {
    String DOMAIN_PERMISSION = "permissions";

    String ENTITY_PERMISSION = "rolePermissions.permission";

    // prettier-ignore
    default Set<String> toEntityAttributes(Set<String> domainAttributes) {
        Set<String> entityAttributes = new HashSet<>();
        domainAttributes.forEach(
            domainAttribute -> {
                if (domainAttribute.equals(DOMAIN_PERMISSION)) {
                    entityAttributes.add(ENTITY_PERMISSION);
                }
            }
        );
        return entityAttributes;
    }

    default RoleEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(id);
        return roleEntity;
    }
}
