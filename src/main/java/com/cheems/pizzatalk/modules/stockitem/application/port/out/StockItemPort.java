package com.cheems.pizzatalk.modules.stockitem.application.port.out;

import com.cheems.pizzatalk.modules.stockitem.domain.StockItem;
import java.util.Set;

public interface StockItemPort {
    StockItem save(StockItem stockItem);

    void deleteById(Long id);

    void removeProductOfStore(Long storeId, Set<Long> productIds);

    void removeOptionDetailOfStore(Long storeId, Set<Long> optionDetailIds);

    void removeAllStockItemsOfStore(Long storeId);

    void removeAllStoreOfProduct(Long productId);

    void removeAllStoreOfOptionDetail(Long optionDetalId);
}
