package com.cheems.pizzatalk.modules.stockitem.application.port.out;

import com.cheems.pizzatalk.modules.stockitem.application.port.in.query.StockItemCriteria;
import com.cheems.pizzatalk.modules.stockitem.domain.StockItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryStockItemPort {
    Optional<StockItem> findByCriteria(StockItemCriteria criteria);

    List<StockItem> findListByCriteria(StockItemCriteria criteria);

    Page<StockItem> findPageByCriteria(StockItemCriteria criteria, Pageable pageable);
}
