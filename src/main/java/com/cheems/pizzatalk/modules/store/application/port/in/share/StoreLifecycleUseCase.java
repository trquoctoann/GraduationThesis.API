package com.cheems.pizzatalk.modules.store.application.port.in.share;

import com.cheems.pizzatalk.modules.store.application.port.in.command.CreateStoreCommand;
import com.cheems.pizzatalk.modules.store.application.port.in.command.UpdateStoreCommand;
import com.cheems.pizzatalk.modules.store.domain.Store;

public interface StoreLifecycleUseCase {
    Store create(CreateStoreCommand command);

    Store update(UpdateStoreCommand command);

    void deleteById(Long id);
}
