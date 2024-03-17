package com.cheems.pizzatalk.modules.stockbatch.adapter.database;

import com.cheems.pizzatalk.entities.mapper.StockBatchMapper;
import com.cheems.pizzatalk.modules.stockbatch.application.port.out.StockBatchPort;
import com.cheems.pizzatalk.modules.stockbatch.domain.StockBatch;
import com.cheems.pizzatalk.repository.StockBatchRepository;
import org.springframework.stereotype.Component;

@Component
public class StockBatchAdapter implements StockBatchPort {

    private final StockBatchRepository stockBatchRepository;

    private final StockBatchMapper stockBatchMapper;

    public StockBatchAdapter(StockBatchRepository stockBatchRepository, StockBatchMapper stockBatchMapper) {
        this.stockBatchRepository = stockBatchRepository;
        this.stockBatchMapper = stockBatchMapper;
    }

    @Override
    public StockBatch save(StockBatch stockBatch) {
        return stockBatchMapper.toDomain(stockBatchRepository.save(stockBatchMapper.toEntity(stockBatch)));
    }

    @Override
    public void deleteById(Long id) {
        stockBatchRepository.deleteById(id);
    }
}
