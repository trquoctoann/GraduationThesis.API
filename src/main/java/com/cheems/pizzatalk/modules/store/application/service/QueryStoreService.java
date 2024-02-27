package com.cheems.pizzatalk.modules.store.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.modules.store.application.port.in.query.StoreCriteria;
import com.cheems.pizzatalk.modules.store.application.port.in.share.QueryStoreUseCase;
import com.cheems.pizzatalk.modules.store.application.port.out.QueryStorePort;
import com.cheems.pizzatalk.modules.store.domain.Store;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class QueryStoreService implements QueryStoreUseCase {

    private final QueryStorePort queryStorePort;

    public QueryStoreService(QueryStorePort queryStorePort) {
        this.queryStorePort = queryStorePort;
    }

    @Override
    public Optional<Store> findById(Long id, String... fetchAttributes) {
        StoreCriteria criteria = new StoreCriteria();

        RangeFilter<Long> idFilter = new RangeFilter<Long>();
        idFilter.setEquals(id);
        criteria.setId(idFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }
        return findByCriteria(criteria);
    }

    @Override
    public Store getById(Long id, String... fetchAttributes) {
        return findById(id, fetchAttributes).orElseThrow(() -> new BusinessException("Not found store with id: " + id));
    }

    @Override
    public Optional<Store> findByCriteria(StoreCriteria criteria) {
        return queryStorePort.findByCriteria(criteria);
    }

    @Override
    public Store getByCriteria(StoreCriteria criteria) {
        return findByCriteria(criteria).orElseThrow(() -> new BusinessException("Not found store with criteria: " + criteria));
    }

    @Override
    public List<Store> findListByCriteria(StoreCriteria criteria) {
        return queryStorePort.findListByCriteria(criteria);
    }

    @Override
    public Page<Store> findPageByCriteria(StoreCriteria criteria, Pageable pageable) {
        return queryStorePort.findPageByCriteria(criteria, pageable);
    }
}
