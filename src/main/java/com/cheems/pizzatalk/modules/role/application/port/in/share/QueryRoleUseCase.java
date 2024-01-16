package com.cheems.pizzatalk.modules.role.application.port.in.share;

import com.cheems.pizzatalk.modules.role.application.port.in.query.RoleCriteria;
import com.cheems.pizzatalk.modules.role.domain.Role;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryRoleUseCase {
    Optional<Role> findById(Long id);

    Role getById(Long id);

    Optional<Role> findByAuthority(String authority);

    Role getByAuthority(String authority);

    Optional<Role> findByCriteria(RoleCriteria criteria);

    List<Role> findListByCriteria(RoleCriteria criteria);

    Page<Role> findPageByCriteria(RoleCriteria criteria, Pageable pageable);
}
