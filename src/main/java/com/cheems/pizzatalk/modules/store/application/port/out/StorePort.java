package com.cheems.pizzatalk.modules.store.application.port.out;

import com.cheems.pizzatalk.modules.store.domain.Store;
import java.util.Set;

public interface StorePort {
    Store save(Store store);

    void deleteById(Long id);

    void saveAll(Set<Store> stores);
}
