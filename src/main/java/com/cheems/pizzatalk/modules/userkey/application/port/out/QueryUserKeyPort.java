package com.cheems.pizzatalk.modules.userkey.application.port.out;

import com.cheems.pizzatalk.modules.userkey.application.port.in.query.UserKeyCriteria;
import com.cheems.pizzatalk.modules.userkey.domain.UserKey;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryUserKeyPort {
    Optional<UserKey> findByCriteria(UserKeyCriteria criteria);

    List<UserKey> findListByCriteria(UserKeyCriteria criteria);

    Page<UserKey> findPageByCriteria(UserKeyCriteria criteria, Pageable pageable);
}
