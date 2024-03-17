package com.cheems.pizzatalk.modules.stockbatch.application.port.out;

import com.cheems.pizzatalk.modules.stockbatch.application.port.in.query.StockBatchCriteria;
import com.cheems.pizzatalk.modules.stockbatch.domain.StockBatch;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryStockBatchPort {
    Optional<StockBatch> findByCriteria(StockBatchCriteria criteria);

    List<StockBatch> findListByCriteria(StockBatchCriteria criteria);

    Page<StockBatch> findPageByCriteria(StockBatchCriteria criteria, Pageable pageable);
}
