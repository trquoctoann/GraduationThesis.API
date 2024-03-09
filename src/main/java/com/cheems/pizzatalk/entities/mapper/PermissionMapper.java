package com.cheems.pizzatalk.entities.mapper;

import com.cheems.pizzatalk.common.mapper.EntityMapper;
import com.cheems.pizzatalk.entities.PermissionEntity;
import com.cheems.pizzatalk.modules.permission.domain.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper extends EntityMapper<Permission, PermissionEntity> {
    default PermissionEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        PermissionEntity permissionEntity = new PermissionEntity();
        permissionEntity.setId(id);
        return permissionEntity;
    }
}
