package com.cheems.pizzatalk.modules.permission.application.port.in.share;

import com.cheems.pizzatalk.modules.permission.application.port.in.query.PermissionCriteria;
import com.cheems.pizzatalk.modules.permission.domain.Permission;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryPermissionUseCase {
    Optional<Permission> findById(Long id, String... fetchAttributes);

    Permission getById(Long id, String... fetchAttributes);

    Optional<Permission> findByName(String name, String... fetchAttributes);

    Permission getByName(String name, String... fetchAttributes);

    Optional<Permission> findByCriteria(PermissionCriteria criteria);

    Permission getByCriteria(PermissionCriteria criteria);

    List<Permission> findListByCriteria(PermissionCriteria criteria);

    Page<Permission> findPageByCriteria(PermissionCriteria criteria, Pageable pageable);

    List<Permission> findListByListPermissionNames(List<String> permissionNames);

    List<Permission> findListByRoleId(Long roleId);
}
