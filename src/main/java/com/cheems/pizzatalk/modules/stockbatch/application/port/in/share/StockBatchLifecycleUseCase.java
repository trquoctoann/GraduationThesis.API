package com.cheems.pizzatalk.modules.stockbatch.application.port.in.share;

import com.cheems.pizzatalk.modules.stockbatch.application.port.in.command.CreateStockBatchCommand;
import com.cheems.pizzatalk.modules.stockbatch.application.port.in.command.UpdateStockBatchCommand;
import com.cheems.pizzatalk.modules.stockbatch.domain.StockBatch;

public interface StockBatchLifecycleUseCase {
    StockBatch create(CreateStockBatchCommand command);

    StockBatch update(UpdateStockBatchCommand command);

    void deleteById(Long id);
}
