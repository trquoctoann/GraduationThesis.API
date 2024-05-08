package com.cheems.pizzatalk.modules.stockbatch.application.service;

import com.cheems.pizzatalk.entities.enumeration.RefillStatus;
import com.cheems.pizzatalk.modules.stockbatch.application.port.in.command.CreateStockBatchCommand;
import com.cheems.pizzatalk.modules.stockbatch.application.port.in.command.UpdateStockBatchCommand;
import com.cheems.pizzatalk.modules.stockbatch.application.port.in.share.QueryStockBatchUseCase;
import com.cheems.pizzatalk.modules.stockbatch.application.port.in.share.StockBatchLifecycleUseCase;
import com.cheems.pizzatalk.modules.stockbatch.application.port.out.StockBatchPort;
import com.cheems.pizzatalk.modules.stockbatch.domain.StockBatch;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StockBatchLifecycleService implements StockBatchLifecycleUseCase {

    private static final Logger log = LoggerFactory.getLogger(StockBatchLifecycleService.class);

    private final ObjectMapper objectMapper;

    private final StockBatchPort stockBatchPort;

    private final QueryStockBatchUseCase queryStockBatchUseCase;

    public StockBatchLifecycleService(
        ObjectMapper objectMapper,
        StockBatchPort stockBatchPort,
        QueryStockBatchUseCase queryStockBatchUseCase
    ) {
        this.objectMapper = objectMapper;
        this.stockBatchPort = stockBatchPort;
        this.queryStockBatchUseCase = queryStockBatchUseCase;
    }

    @Override
    public StockBatch create(CreateStockBatchCommand command) {
        log.debug("Creating stock batch with command: {}", command);

        StockBatch stockBatch = objectMapper.convertValue(command, StockBatch.class);
        stockBatch.setStatus(RefillStatus.ORDERED);
        stockBatch.setOrderedDate(Instant.now());

        stockBatch = stockBatchPort.save(stockBatch);
        log.debug("Created stock batch with command: {}", command);
        return stockBatch;
    }

    @Override
    public StockBatch update(UpdateStockBatchCommand command) {
        log.debug("Updating stock batch with id: {}", command.getId());
        StockBatch existStockBatch = queryStockBatchUseCase.getById(command.getId());

        StockBatch stockBatch = objectMapper.convertValue(command, StockBatch.class);
        stockBatch.setStatus(existStockBatch.getStatus());
        stockBatch.setOrderedQuantity(existStockBatch.getOrderedQuantity());
        stockBatch.setOrderedDate(existStockBatch.getOrderedDate());
        stockBatch.setStockItemId(existStockBatch.getStockItemId());

        stockBatch = stockBatchPort.save(stockBatch);
        log.debug("Updated stock batch with id: {}", command.getId());
        return stockBatch;
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Deleting stock batch id: {}", id);
        stockBatchPort.deleteById(id);
        log.debug("Deleted stock batch id: {}", id);
    }
}
