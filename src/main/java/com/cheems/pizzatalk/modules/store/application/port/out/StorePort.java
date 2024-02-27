package com.cheems.pizzatalk.modules.store.application.port.out;

import com.cheems.pizzatalk.modules.store.domain.Store;

public interface StorePort {
    Store save(Store store);

    void deleteById(Long id);
}
