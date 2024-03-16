package com.cheems.pizzatalk.modules.stockitem.application.port.in.share;

import com.cheems.pizzatalk.modules.stockitem.application.port.in.command.CreateStockItemCommand;
import com.cheems.pizzatalk.modules.stockitem.application.port.in.command.UpdateStockItemCommand;
import com.cheems.pizzatalk.modules.stockitem.domain.StockItem;
import java.util.Set;

public interface StockItemLifecycleUseCase {
    StockItem create(CreateStockItemCommand command);

    StockItem update(UpdateStockItemCommand command);

    StockItem updatePrice(Long stockItemId, Float newPrice);

    StockItem updateQuantity(Long stockItemId, Long newQuantity);

    void deleteById(Long id);

    void removeProductOfStore(Long storeId, Set<Long> productIds);

    void removeOptionDetailOfStore(Long storeId, Set<Long> optionDetailIds);

    void removeAllStockItemsOfStore(Long storeId);

    void removeAllStoreOfProduct(Long productId);

    void removeAllStoreOfOptionDetail(Long optionDetalId);
}
