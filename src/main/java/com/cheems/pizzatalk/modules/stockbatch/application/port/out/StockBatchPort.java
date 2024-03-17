package com.cheems.pizzatalk.modules.stockbatch.application.port.out;

import com.cheems.pizzatalk.modules.stockbatch.domain.StockBatch;

public interface StockBatchPort {
    StockBatch save(StockBatch stockBatch);

    void deleteById(Long id);
}
