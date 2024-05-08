package com.cheems.pizzatalk.modules.stockitem.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.modules.optiondetail.application.port.in.share.QueryOptionDetailUseCase;
import com.cheems.pizzatalk.modules.optiondetail.domain.OptionDetail;
import com.cheems.pizzatalk.modules.product.application.port.in.share.QueryProductUseCase;
import com.cheems.pizzatalk.modules.product.domain.Product;
import com.cheems.pizzatalk.modules.stockitem.application.port.in.command.CreateStockItemCommand;
import com.cheems.pizzatalk.modules.stockitem.application.port.in.command.UpdateStockItemCommand;
import com.cheems.pizzatalk.modules.stockitem.application.port.in.share.QueryStockItemUseCase;
import com.cheems.pizzatalk.modules.stockitem.application.port.in.share.StockItemLifecycleUseCase;
import com.cheems.pizzatalk.modules.stockitem.application.port.out.StockItemPort;
import com.cheems.pizzatalk.modules.stockitem.domain.StockItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StockItemLifecycleService implements StockItemLifecycleUseCase {

    private static final Logger log = LoggerFactory.getLogger(StockItemLifecycleService.class);

    private final ObjectMapper objectMapper;

    private final StockItemPort stockItemPort;

    private final QueryStockItemUseCase queryStockItemUseCase;

    private final QueryProductUseCase queryProductUseCase;

    private final QueryOptionDetailUseCase queryOptionDetailUseCase;

    public StockItemLifecycleService(
        ObjectMapper objectMapper,
        StockItemPort stockItemPort,
        QueryStockItemUseCase queryStockItemUseCase,
        QueryProductUseCase queryProductUseCase,
        QueryOptionDetailUseCase queryOptionDetailUseCase
    ) {
        this.objectMapper = objectMapper;
        this.stockItemPort = stockItemPort;
        this.queryStockItemUseCase = queryStockItemUseCase;
        this.queryProductUseCase = queryProductUseCase;
        this.queryOptionDetailUseCase = queryOptionDetailUseCase;
    }

    @Override
    public StockItem create(CreateStockItemCommand command) {
        log.debug("Saving stock item: {} to store id: {}", command, command.getStoreId());
        if (command.getProductId() != null && command.getOptionDetailId() != null) {
            throw new BusinessException("Item must be specified as either a product or an option detail, but not both");
        } else if (command.getProductId() == null && command.getOptionDetailId() == null) {
            throw new BusinessException("Item must not be null");
        }

        if (
            (command.getProductId() != null &&
                queryStockItemUseCase.findByStoreIdAndProductId(command.getStoreId(), command.getProductId()).isPresent()) ||
            (command.getOptionDetailId() != null &&
                queryStockItemUseCase.findByStoreIdAndOptionDetailId(command.getStoreId(), command.getOptionDetailId()).isPresent())
        ) {
            throw new BusinessException("Item already assigned to this store");
        }
        String[] stockInformation = getSkuOfStockItem(command);

        StockItem stockItem = objectMapper.convertValue(command, StockItem.class);

        if (stockItem != null) {
            stockItem.setName(stockInformation[0]);
            stockItem.setSku(stockInformation[1]);
        }

        stockItem = stockItemPort.save(stockItem);
        log.debug("Saved stock item: {} to store id: {}", command, command.getStoreId());
        return stockItem;
    }

    @Override
    public StockItem update(UpdateStockItemCommand command) {
        log.debug("Updating stock item id: {}", command.getId());
        StockItem existStockItem = queryStockItemUseCase.getById(command.getId());

        StockItem stockItem = objectMapper.convertValue(command, StockItem.class);
        stockItem.setId(existStockItem.getId());
        stockItem.setName(existStockItem.getName());
        stockItem.setSku(existStockItem.getSku());
        stockItem.setUnit(existStockItem.getUnit());
        stockItem.setTotalQuantity(existStockItem.getTotalQuantity());
        stockItem.setSellingPrice(existStockItem.getSellingPrice());
        stockItem.setStoreId(existStockItem.getStoreId());
        stockItem.setProductId(existStockItem.getProductId());
        stockItem.setOptionDetailId(existStockItem.getOptionDetailId());

        stockItem = stockItemPort.save(stockItem);
        log.debug("Updated stock item id: {}", command.getId());
        return stockItem;
    }

    @Override
    public StockItem updatePrice(Long stockItemId, Float newPrice) {
        StockItem existStockItem = queryStockItemUseCase.getById(stockItemId);
        log.debug("Updating price of product id: {} of store id: {}", existStockItem.getProductId(), existStockItem.getStoreId());

        existStockItem.setSellingPrice(newPrice);

        existStockItem = stockItemPort.save(existStockItem);
        log.debug("Updated price of product id: {} of store id: {}", existStockItem.getProductId(), existStockItem.getStoreId());
        return existStockItem;
    }

    @Override
    public StockItem updateQuantity(Long stockItemId, Long newQuantity) {
        // StockItem existStockItem = queryStockItemUseCase.getById(stockItemId);
        // log.debug("Updating quantity of product id: {} of store id: {}", existStockItem.getProductId(), existStockItem.getStoreId());

        // existStockItem = stockItemPort.save(existStockItem);
        // log.debug("Updated quantity of product id: {} of store id: {}", existStockItem.getProductId(), existStockItem.getStoreId());
        // return existStockItem;
        return null;
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Deleting stock item id: {}", id);
        stockItemPort.deleteById(id);
        log.debug("Deleted stock item id: {}", id);
    }

    @Override
    public void removeProductOfStore(Long storeId, Set<Long> productIds) {
        log.debug("Removing product ids: {} from store id: {}", productIds, storeId);
        stockItemPort.removeProductOfStore(storeId, productIds);
        log.debug("Removed product ids: {} from store id: {}", productIds, storeId);
    }

    @Override
    public void removeOptionDetailOfStore(Long storeId, Set<Long> optionDetailIds) {
        log.debug("Removing option detail id: {} from store id: {}", optionDetailIds, storeId);
        stockItemPort.removeOptionDetailOfStore(storeId, optionDetailIds);
        log.debug("Removed option detail id: {} from store id: {}", optionDetailIds, storeId);
    }

    @Override
    public void removeAllStockItemsOfStore(Long storeId) {
        log.debug("Removing all stock item from store id: {}", storeId);
        stockItemPort.removeAllStockItemsOfStore(storeId);
        log.debug("Removed all stock item from store id: {}", storeId);
    }

    @Override
    public void removeAllStoreOfProduct(Long productId) {
        log.debug("Removing product id: {} from all stores selling it", productId);
        stockItemPort.removeAllStoreOfProduct(productId);
        log.debug("Removed product id: {} from all stores selling it", productId);
    }

    @Override
    public void removeAllStoreOfOptionDetail(Long optionDetalId) {
        log.debug("Removing option detail id: {} from all stores selling it", optionDetalId);
        stockItemPort.removeAllStoreOfOptionDetail(optionDetalId);
        log.debug("Removed option detail id: {} from all stores selling it", optionDetalId);
    }

    private <T extends CreateStockItemCommand> String[] getSkuOfStockItem(T command) {
        if (command.getProductId() != null) {
            Product product = queryProductUseCase.getById(command.getProductId());
            return new String[] { product.getName(), product.getSku() };
        }
        if (command.getOptionDetailId() != null) {
            OptionDetail optionDetail = queryOptionDetailUseCase.getById(command.getOptionDetailId());
            return new String[] { optionDetail.getName(), optionDetail.getSku() };
        }
        return null;
    }
}
