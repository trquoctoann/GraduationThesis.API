package com.cheems.pizzatalk.modules.user.application.port.out;

import com.cheems.pizzatalk.modules.user.application.port.in.query.UserCriteria;
import com.cheems.pizzatalk.modules.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryUserPort {
    Optional<User> findByCriteria(UserCriteria criteria);

    List<User> findListByCriteria(UserCriteria criteria);

    Page<User> findPageByCriteria(UserCriteria criteria, Pageable pageable);
}
