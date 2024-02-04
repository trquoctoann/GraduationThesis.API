package com.cheems.pizzatalk.modules.user.application.port.in.share;

import com.cheems.pizzatalk.modules.user.application.port.in.query.UserCriteria;
import com.cheems.pizzatalk.modules.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryUserUseCase {
    Optional<User> findById(Long id, String... fetchAttributes);

    User getById(Long id, String... fetchAttributes);

    Optional<User> findByUsername(String username, String... fetchAttributes);

    User getByUsername(String username, String... fetchAttributes);

    Optional<User> findByEmail(String email, String... fetchAttributes);

    User getByEmail(String username, String... fetchAttributes);

    Optional<User> findByCriteria(UserCriteria criteria);

    User getByCriteria(UserCriteria criteria);

    List<User> findListByCriteria(UserCriteria criteria);

    Page<User> findPageByCriteria(UserCriteria criteria, Pageable pageable);
}
