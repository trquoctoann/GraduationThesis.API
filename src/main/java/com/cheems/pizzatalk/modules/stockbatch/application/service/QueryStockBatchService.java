package com.cheems.pizzatalk.modules.stockbatch.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.modules.stockbatch.application.port.in.query.StockBatchCriteria;
import com.cheems.pizzatalk.modules.stockbatch.application.port.in.share.QueryStockBatchUseCase;
import com.cheems.pizzatalk.modules.stockbatch.application.port.out.QueryStockBatchPort;
import com.cheems.pizzatalk.modules.stockbatch.domain.StockBatch;
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
public class QueryStockBatchService implements QueryStockBatchUseCase {

    private final QueryStockBatchPort queryStockBatchPort;

    public QueryStockBatchService(QueryStockBatchPort queryStockBatchPort) {
        this.queryStockBatchPort = queryStockBatchPort;
    }

    @Override
    public Optional<StockBatch> findById(Long id, String... fetchAttributes) {
        StockBatchCriteria criteria = new StockBatchCriteria();

        RangeFilter<Long> idFilter = new RangeFilter<>();
        idFilter.setEquals(id);

        criteria.setId(idFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }

        return findByCriteria(criteria);
    }

    @Override
    public StockBatch getById(Long id, String... fetchAttributes) {
        return findById(id, fetchAttributes).orElseThrow(() -> new BusinessException("Not found stock batch with id: " + id));
    }

    @Override
    public Optional<StockBatch> findByCriteria(StockBatchCriteria criteria) {
        return queryStockBatchPort.findByCriteria(criteria);
    }

    @Override
    public StockBatch getByCriteria(StockBatchCriteria criteria) {
        return findByCriteria(criteria)
            .orElseThrow(() -> new BusinessException("Not found stock batch with criteria" + criteria.toString()));
    }

    @Override
    public List<StockBatch> findListByCriteria(StockBatchCriteria criteria) {
        return queryStockBatchPort.findListByCriteria(criteria);
    }

    @Override
    public Page<StockBatch> findPageByCriteria(StockBatchCriteria criteria, Pageable pageable) {
        return queryStockBatchPort.findPageByCriteria(criteria, pageable);
    }
}
