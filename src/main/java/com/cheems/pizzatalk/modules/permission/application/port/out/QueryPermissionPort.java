package com.cheems.pizzatalk.modules.permission.application.port.out;

import com.cheems.pizzatalk.modules.permission.application.port.in.query.PermissionCriteria;
import com.cheems.pizzatalk.modules.permission.domain.Permission;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryPermissionPort {
    Optional<Permission> findByCriteria(PermissionCriteria criteria);

    List<Permission> findListByCriteria(PermissionCriteria criteria);

    Page<Permission> findPageByCriteria(PermissionCriteria criteria, Pageable pageable);
}
