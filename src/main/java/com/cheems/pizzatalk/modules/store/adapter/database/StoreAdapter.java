package com.cheems.pizzatalk.modules.store.adapter.database;

import com.cheems.pizzatalk.entities.mapper.StoreMapper;
import com.cheems.pizzatalk.modules.store.application.port.out.StorePort;
import com.cheems.pizzatalk.modules.store.domain.Store;
import com.cheems.pizzatalk.repository.StoreRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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

    @Override
    public void saveAll(Set<Store> stores) {
        List<Store> listStores = new ArrayList<>(stores);
        storeRepository.saveAll(storeMapper.toEntity(listStores));
    }
}
