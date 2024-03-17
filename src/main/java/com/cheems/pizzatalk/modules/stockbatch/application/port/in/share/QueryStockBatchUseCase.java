package com.cheems.pizzatalk.modules.stockbatch.application.port.in.share;

import com.cheems.pizzatalk.modules.stockbatch.application.port.in.query.StockBatchCriteria;
import com.cheems.pizzatalk.modules.stockbatch.domain.StockBatch;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryStockBatchUseCase {
    Optional<StockBatch> findById(Long id, String... fetchAttributes);

    StockBatch getById(Long id, String... fetchAttributes);

    Optional<StockBatch> findByCriteria(StockBatchCriteria criteria);

    StockBatch getByCriteria(StockBatchCriteria criteria);

    List<StockBatch> findListByCriteria(StockBatchCriteria criteria);

    Page<StockBatch> findPageByCriteria(StockBatchCriteria criteria, Pageable pageable);
}
