package com.cheems.pizzatalk.modules.store.application.port.in.share;

import com.cheems.pizzatalk.modules.store.application.port.in.query.StoreCriteria;
import com.cheems.pizzatalk.modules.store.domain.Store;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryStoreUseCase {
    Optional<Store> findById(Long id, String... fetchAttributes);

    Store getById(Long id, String... fetchAttributes);

    Optional<Store> findByCriteria(StoreCriteria criteria);

    Store getByCriteria(StoreCriteria criteria);

    List<Store> findListByCriteria(StoreCriteria criteria);

    Page<Store> findPageByCriteria(StoreCriteria criteria, Pageable pageable);
}
