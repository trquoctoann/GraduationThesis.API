package com.cheems.pizzatalk.modules.store.application.port.out;

import com.cheems.pizzatalk.modules.store.application.port.in.query.StoreCriteria;
import com.cheems.pizzatalk.modules.store.domain.Store;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryStorePort {
    Optional<Store> findByCriteria(StoreCriteria criteria);

    List<Store> findListByCriteria(StoreCriteria criteria);

    Page<Store> findPageByCriteria(StoreCriteria criteria, Pageable pageable);
}
