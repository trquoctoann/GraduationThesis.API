package com.cheems.pizzatalk.modules.role.application.port.in.share;

import com.cheems.pizzatalk.modules.role.application.port.in.query.RoleCriteria;
import com.cheems.pizzatalk.modules.role.domain.Role;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryRoleUseCase {
    Optional<Role> findById(Long id, String... fetchAttributes);

    Role getById(Long id, String... fetchAttributes);

    Optional<Role> findByName(String name, String... fetchAttributes);

    Role getByName(String name, String... fetchAttributes);

    Optional<Role> findByCriteria(RoleCriteria criteria);

    List<Role> findListByCriteria(RoleCriteria criteria);

    Page<Role> findPageByCriteria(RoleCriteria criteria, Pageable pageable);

    List<Role> findListByListName(List<String> names);

    List<Role> findListByUserId(Long userId);
}
