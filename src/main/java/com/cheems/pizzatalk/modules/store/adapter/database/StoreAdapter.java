package com.cheems.pizzatalk.modules.store.adapter.database;

import com.cheems.pizzatalk.entities.mapper.StoreMapper;
import com.cheems.pizzatalk.modules.store.application.port.out.StorePort;
import com.cheems.pizzatalk.modules.store.domain.Store;
import com.cheems.pizzatalk.repository.StoreRepository;
import org.springframework.stereotype.Component;

@Component
public class StoreAdapter implements StorePort {

    private final StoreRepository storeRepository;

    private final StoreMapper storeMapper;

    public StoreAdapter(StoreRepository storeRepository, StoreMapper storeMapper) {
        this.storeRepository = storeRepository;
        this.storeMapper = storeMapper;
    }

    @Override
    public Store save(Store store) {
        return storeMapper.toDomain(storeRepository.save(storeMapper.toEntity(store)));
    }

    @Override
    public void deleteById(Long id) {
        storeRepository.deleteById(id);
    }
}
