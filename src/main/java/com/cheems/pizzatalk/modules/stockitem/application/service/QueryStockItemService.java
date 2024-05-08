package com.cheems.pizzatalk.modules.stockitem.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.modules.stockitem.application.port.in.query.StockItemCriteria;
import com.cheems.pizzatalk.modules.stockitem.application.port.in.share.QueryStockItemUseCase;
import com.cheems.pizzatalk.modules.stockitem.application.port.out.QueryStockItemPort;
import com.cheems.pizzatalk.modules.stockitem.domain.StockItem;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class QueryStockItemService implements QueryStockItemUseCase {

    private final QueryStockItemPort queryStockItemPort;

    public QueryStockItemService(QueryStockItemPort queryStockItemPort) {
        this.queryStockItemPort = queryStockItemPort;
    }

    @Override
    public Optional<StockItem> findById(Long id, String... fetchAttributes) {
        StockItemCriteria criteria = new StockItemCriteria();

        RangeFilter<Long> idFilter = new RangeFilter<>();
        idFilter.setEquals(id);

        criteria.setId(idFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }

        return findByCriteria(criteria);
    }

    @Override
    public StockItem getById(Long id, String... fetchAttributes) {
        return findById(id, fetchAttributes).orElseThrow(() -> new BusinessException("Not found stock item with id: " + id));
    }

    @Override
    public Optional<StockItem> findByStoreIdAndProductId(Long storeId, Long productId, String... fetchAttributes) {
        StockItemCriteria criteria = new StockItemCriteria();

        RangeFilter<Long> storeIdFilter = new RangeFilter<>();
        storeIdFilter.setEquals(storeId);

        RangeFilter<Long> productIdFilter = new RangeFilter<>();
        productIdFilter.setEquals(productId);

        criteria.setStoreId(storeIdFilter);
        criteria.setProductId(productIdFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }

        return findByCriteria(criteria);
    }

    @Override
    public StockItem getByStoreIdAndProductId(Long storeId, Long productId, String... fetchAttributes) {
        return findByStoreIdAndProductId(storeId, productId, fetchAttributes)
            .orElseThrow(() -> new BusinessException("Not found stock item with store id: " + storeId + "and product id: " + productId));
    }

    @Override
    public Optional<StockItem> findByStoreIdAndOptionDetailId(Long storeId, Long optionDetailId, String... fetchAttributes) {
        StockItemCriteria criteria = new StockItemCriteria();

        RangeFilter<Long> storeIdFilter = new RangeFilter<>();
        storeIdFilter.setEquals(storeId);

        RangeFilter<Long> optionDetailIdFilter = new RangeFilter<>();
        optionDetailIdFilter.setEquals(optionDetailId);

        criteria.setStoreId(storeIdFilter);
        criteria.setOptionDetailId(optionDetailIdFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }

        return findByCriteria(criteria);
    }

    @Override
    public Optional<StockItem> findByCriteria(StockItemCriteria criteria) {
        return queryStockItemPort.findByCriteria(criteria);
    }

    @Override
    public StockItem getByCriteria(StockItemCriteria criteria) {
        return findByCriteria(criteria)
            .orElseThrow(() -> new BusinessException("Not found stock item with criteria" + criteria.toString()));
    }

    @Override
    public List<StockItem> findListByCriteria(StockItemCriteria criteria) {
        return queryStockItemPort.findListByCriteria(criteria);
    }

    @Override
    public Page<StockItem> findPageByCriteria(StockItemCriteria criteria, Pageable pageable) {
        return queryStockItemPort.findPageByCriteria(criteria, pageable);
    }

    @Override
    public List<StockItem> findListByProductId(Long productId) {
        StockItemCriteria criteria = new StockItemCriteria();

        RangeFilter<Long> productIdFilter = new RangeFilter<>();
        productIdFilter.setEquals(productId);

        criteria.setProductId(productIdFilter);
        return queryStockItemPort.findListByCriteria(criteria);
    }

    @Override
    public List<StockItem> findListByStoreId(Long storeId) {
        StockItemCriteria criteria = new StockItemCriteria();

        RangeFilter<Long> storeIdFilter = new RangeFilter<>();
        storeIdFilter.setEquals(storeId);

        criteria.setStoreId(storeIdFilter);
        return queryStockItemPort.findListByCriteria(criteria);
    }

    @Override
    public List<StockItem> findListByStoreIdAndProductId(Long storeId, Long productId) {
        StockItemCriteria criteria = new StockItemCriteria();

        RangeFilter<Long> storeIdFilter = new RangeFilter<>();
        storeIdFilter.setEquals(storeId);

        RangeFilter<Long> productIdFilter = new RangeFilter<>();
        productIdFilter.setEquals(productId);

        criteria.setStoreId(storeIdFilter);
        criteria.setProductId(productIdFilter);
        return queryStockItemPort.findListByCriteria(criteria);
    }

    @Override
    public List<StockItem> findListByStoreIdAndOptionDetailId(Long storeId, Long optionDetailId) {
        StockItemCriteria criteria = new StockItemCriteria();

        RangeFilter<Long> storeIdFilter = new RangeFilter<>();
        storeIdFilter.setEquals(storeId);

        RangeFilter<Long> optionDetailIdFilter = new RangeFilter<>();
        optionDetailIdFilter.setEquals(optionDetailId);

        criteria.setStoreId(storeIdFilter);
        criteria.setOptionDetailId(optionDetailIdFilter);
        return queryStockItemPort.findListByCriteria(criteria);
    }
}
