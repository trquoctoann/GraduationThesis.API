package com.cheems.pizzatalk.modules.stockitem.application.port.in.share;

import com.cheems.pizzatalk.modules.stockitem.application.port.in.query.StockItemCriteria;
import com.cheems.pizzatalk.modules.stockitem.domain.StockItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryStockItemUseCase {
    Optional<StockItem> findById(Long id, String... fetchAttributes);

    StockItem getById(Long id, String... fetchAttributes);

    Optional<StockItem> findByStoreIdAndProductId(Long storeId, Long productId, String... fetchAttributes);

    StockItem getByStoreIdAndProductId(Long storeId, Long productId, String... fetchAttributes);

    Optional<StockItem> findByStoreIdAndOptionDetailId(Long storeId, Long optionDetailId, String... fetchAttributes);

    Optional<StockItem> findByCriteria(StockItemCriteria criteria);

    StockItem getByCriteria(StockItemCriteria criteria);

    List<StockItem> findListByCriteria(StockItemCriteria criteria);

    Page<StockItem> findPageByCriteria(StockItemCriteria criteria, Pageable pageable);

    List<StockItem> findListByStoreId(Long storeId);

    List<StockItem> findListByProductId(Long productId);

    List<StockItem> findListByStoreIdAndProductId(Long storeId, Long productId);

    List<StockItem> findListByStoreIdAndOptionDetailId(Long storeId, Long optionDetailId);
}
