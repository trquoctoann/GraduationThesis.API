package com.cheems.pizzatalk.modules.store.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.entities.enumeration.OperationalStatus;
import com.cheems.pizzatalk.modules.area.application.port.in.share.QueryAreaUseCase;
import com.cheems.pizzatalk.modules.area.application.port.out.AreaPort;
import com.cheems.pizzatalk.modules.area.domain.Area;
import com.cheems.pizzatalk.modules.store.application.port.in.command.CreateStoreCommand;
import com.cheems.pizzatalk.modules.store.application.port.in.command.UpdateStoreCommand;
import com.cheems.pizzatalk.modules.store.application.port.in.share.QueryStoreUseCase;
import com.cheems.pizzatalk.modules.store.application.port.in.share.StoreLifecycleUseCase;
import com.cheems.pizzatalk.modules.store.application.port.out.StorePort;
import com.cheems.pizzatalk.modules.store.domain.Store;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StoreLifecycleService implements StoreLifecycleUseCase {

    private static final Logger log = LoggerFactory.getLogger(StoreLifecycleService.class);

    private final ObjectMapper objectMapper;

    private final StorePort storePort;

    private final AreaPort areaPort;

    private final QueryStoreUseCase queryStoreUseCase;

    private final QueryAreaUseCase queryAreaUseCase;

    public StoreLifecycleService(
        ObjectMapper objectMapper,
        StorePort storePort,
        AreaPort areaPort,
        QueryStoreUseCase queryStoreUseCase,
        QueryAreaUseCase queryAreaUseCase
    ) {
        this.objectMapper = objectMapper;
        this.storePort = storePort;
        this.areaPort = areaPort;
        this.queryStoreUseCase = queryStoreUseCase;
        this.queryAreaUseCase = queryAreaUseCase;
    }

    @Override
    public Store create(CreateStoreCommand command) {
        log.debug("Creating store: {}", command);
        Area existArea = queryAreaUseCase.getById(command.getAreaId());
        if (existArea.getStatus() == OperationalStatus.CLOSED) {
            throw new BusinessException("Area has area code: " + command.getAreaId() + "is closed permanently.");
        }
        existArea.setStoreCount(existArea.getStoreCount() + 1);

        Store store = objectMapper.convertValue(command, Store.class);
        store = storePort.save(store);
        existArea = areaPort.save(existArea);

        log.debug("Created store: {}", command);
        return store;
    }

    @Override
    public Store update(UpdateStoreCommand command) {
        log.debug("Updating store, id: {}", command.getId());
        Store existStore = queryStoreUseCase.getById(command.getId());

        Store store = objectMapper.convertValue(command, Store.class);
        store.setId(existStore.getId());
        store.setOriginalId(existStore.getOriginalId());

        log.debug("Updated store, id: {}", command.getId());
        return store;
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Deleting store, id: {}", id);
        Store deletingStore = queryStoreUseCase.getById(id);

        Area areaOfDeletingStore = queryAreaUseCase.getById(deletingStore.getAreaId());
        areaOfDeletingStore.setStoreCount(areaOfDeletingStore.getStoreCount() - 1);
        areaPort.save(areaOfDeletingStore);

        storePort.deleteById(id);
        log.debug("Deleted store, id: {}", id);
    }
}
