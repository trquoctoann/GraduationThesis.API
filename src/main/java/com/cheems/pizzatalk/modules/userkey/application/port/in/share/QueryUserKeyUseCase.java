package com.cheems.pizzatalk.modules.userkey.application.port.in.share;

import com.cheems.pizzatalk.entities.enumeration.UserKeyType;
import com.cheems.pizzatalk.modules.userkey.application.port.in.query.UserKeyCriteria;
import com.cheems.pizzatalk.modules.userkey.domain.UserKey;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryUserKeyUseCase {
    Optional<UserKey> findById(Long id, String... fetchAttributes);

    UserKey getById(Long id, String... fetchAttributes);

    Optional<UserKey> findByValue(String value, String... fetchAttributes);

    UserKey getByValue(String value, String... fetchAttributes);

    Optional<UserKey> findByCriteria(UserKeyCriteria criteria);

    UserKey getByCriteria(UserKeyCriteria criteria);

    List<UserKey> findListByCriteria(UserKeyCriteria criteria);

    Page<UserKey> findPageByCriteria(UserKeyCriteria criteria, Pageable pageable);

    List<UserKey> findActiveKeyByUserId(UserKeyType type, Long userId);
}
