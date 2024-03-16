package com.cheems.pizzatalk.modules.stockitem.adapter.database;

import com.cheems.pizzatalk.entities.StockItemEntity;
import com.cheems.pizzatalk.entities.mapper.StockItemMapper;
import com.cheems.pizzatalk.modules.stockitem.application.port.out.StockItemPort;
import com.cheems.pizzatalk.modules.stockitem.domain.StockItem;
import com.cheems.pizzatalk.repository.StockItemRepository;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class ProductStockItemAdapter implements StockItemPort {

    private final StockItemRepository stockItemRepository;

    private final StockItemMapper stockItemMapper;

    public ProductStockItemAdapter(StockItemRepository stockItemRepository, StockItemMapper stockItemMapper) {
        this.stockItemRepository = stockItemRepository;
        this.stockItemMapper = stockItemMapper;
    }

    @Override
    public StockItem save(StockItem stockItem) {
        return stockItemMapper.toDomain(stockItemRepository.save(stockItemMapper.toEntity(stockItem)));
    }

    @Override
    public void deleteById(Long id) {
        stockItemRepository.deleteById(id);
    }

    @Override
    public void removeProductOfStore(Long storeId, Set<Long> productIds) {
        Set<StockItemEntity> existStockItemEntities = stockItemRepository.findByStoreIdAndProductIds(storeId, productIds);
        stockItemRepository.deleteAll(existStockItemEntities);
    }

    @Override
    public void removeOptionDetailOfStore(Long storeId, Set<Long> optionDetailIds) {
        Set<StockItemEntity> existStockItemEntities = stockItemRepository.findByStoreIdAndOptionDetailIds(storeId, optionDetailIds);
        stockItemRepository.deleteAll(existStockItemEntities);
    }

    @Override
    public void removeAllStockItemsOfStore(Long storeId) {
        stockItemRepository.deleteByStoreId(storeId);
    }

    @Override
    public void removeAllStoreOfProduct(Long productId) {
        stockItemRepository.deleteByProductId(productId);
    }

    @Override
    public void removeAllStoreOfOptionDetail(Long optionDetalId) {
        stockItemRepository.deleteByOptionDetailId(optionDetalId);
    }
}
