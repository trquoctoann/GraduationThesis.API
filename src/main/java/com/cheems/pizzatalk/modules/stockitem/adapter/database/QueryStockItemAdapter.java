package com.cheems.pizzatalk.modules.stockitem.adapter.database;

import com.cheems.pizzatalk.common.exception.AdapterException;
import com.cheems.pizzatalk.common.service.QueryService;
import com.cheems.pizzatalk.common.specification.SpecificationUtils;
import com.cheems.pizzatalk.entities.OptionDetailEntity_;
import com.cheems.pizzatalk.entities.ProductEntity_;
import com.cheems.pizzatalk.entities.StockBatchEntity_;
import com.cheems.pizzatalk.entities.StockItemEntity;
import com.cheems.pizzatalk.entities.StockItemEntity_;
import com.cheems.pizzatalk.entities.StoreEntity_;
import com.cheems.pizzatalk.entities.mapper.OptionDetailMapper;
import com.cheems.pizzatalk.entities.mapper.ProductMapper;
import com.cheems.pizzatalk.entities.mapper.StockBatchMapper;
import com.cheems.pizzatalk.entities.mapper.StockItemMapper;
import com.cheems.pizzatalk.entities.mapper.StoreMapper;
import com.cheems.pizzatalk.modules.stockitem.application.port.in.query.StockItemCriteria;
import com.cheems.pizzatalk.modules.stockitem.application.port.out.QueryStockItemPort;
import com.cheems.pizzatalk.modules.stockitem.domain.StockItem;
import com.cheems.pizzatalk.repository.StockItemRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.criteria.JoinType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class QueryStockItemAdapter extends QueryService<StockItemEntity> implements QueryStockItemPort {

    private final StockItemRepository stockItemRepository;

    private final StockItemMapper stockItemMapper;

    private final StoreMapper storeMapper;

    private final ProductMapper productMapper;

    private final OptionDetailMapper optionDetailMapper;

    private final StockBatchMapper stockBatchMapper;

    public QueryStockItemAdapter(
        StockItemRepository stockItemRepository,
        StockItemMapper stockItemMapper,
        StoreMapper storeMapper,
        ProductMapper productMapper,
        OptionDetailMapper optionDetailMapper,
        StockBatchMapper stockBatchMapper
    ) {
        this.stockItemRepository = stockItemRepository;
        this.stockItemMapper = stockItemMapper;
        this.storeMapper = storeMapper;
        this.productMapper = productMapper;
        this.optionDetailMapper = optionDetailMapper;
        this.stockBatchMapper = stockBatchMapper;
    }

    @Override
    public Optional<StockItem> findByCriteria(StockItemCriteria criteria) {
        List<StockItemEntity> stockItemEntities = stockItemRepository.findAll(createSpecification(criteria));
        if (CollectionUtils.isEmpty(stockItemEntities)) {
            return Optional.empty();
        }

        Integer stockItemEntitiesSize = stockItemEntities.size();
        if (stockItemEntitiesSize > 1) {
            Set<Long> stockItemIds = stockItemEntities.stream().map(StockItemEntity::getId).collect(Collectors.toSet());
            throw new AdapterException("Found more than one stock item: " + stockItemIds);
        }
        return Optional.of(stockItemEntities.get(0)).map(stockItemEntity -> toDomainModel(stockItemEntity, criteria.getFetchAttributes()));
    }

    @Override
    public List<StockItem> findListByCriteria(StockItemCriteria criteria) {
        return stockItemRepository
            .findAll(createSpecification(criteria))
            .stream()
            .map(stockItemEntity -> toDomainModel(stockItemEntity, criteria.getFetchAttributes()))
            .collect(Collectors.toList());
    }

    @Override
    public Page<StockItem> findPageByCriteria(StockItemCriteria criteria, Pageable pageable) {
        Page<StockItemEntity> stockItemEntitiesPage = stockItemRepository.findAll(createSpecification(criteria), pageable);
        return new PageImpl<>(
            stockItemMapper.toDomain(stockItemEntitiesPage.getContent()),
            pageable,
            stockItemEntitiesPage.getTotalElements()
        );
    }

    private Specification<StockItemEntity> createSpecification(StockItemCriteria criteria) {
        Set<String> fetchAttributes = criteria.getFetchAttributes();
        Specification<StockItemEntity> specification = Specification.where(null);

        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), StockItemEntity_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), StockItemEntity_.name));
            }
            if (criteria.getSku() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSku(), StockItemEntity_.sku));
            }
            if (criteria.getUnit() != null) {
                specification = specification.and(buildSpecification(criteria.getUnit(), StockItemEntity_.unit));
            }
            if (criteria.getTotalQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalQuantity(), StockItemEntity_.totalQuantity));
            }
            if (criteria.getReorderLevel() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReorderLevel(), StockItemEntity_.reorderLevel));
            }
            if (criteria.getReorderQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReorderQuantity(), StockItemEntity_.reorderQuantity));
            }
            if (criteria.getSellingPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSellingPrice(), StockItemEntity_.sellingPrice));
            }
            if (criteria.getStoreId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStoreId(),
                            root -> root.join(StockItemEntity_.store, JoinType.LEFT).get(StoreEntity_.id)
                        )
                    );
            }
            if (criteria.getProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductId(),
                            root -> root.join(StockItemEntity_.product, JoinType.LEFT).get(ProductEntity_.id)
                        )
                    );
            }
            if (criteria.getOptionDetailId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOptionDetailId(),
                            root -> root.join(StockItemEntity_.optionDetail, JoinType.LEFT).get(OptionDetailEntity_.id)
                        )
                    );
            }
            if (criteria.getStockBatchId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStockBatchId(),
                            root -> root.join(StockItemEntity_.stockBatches, JoinType.LEFT).get(StockBatchEntity_.id)
                        )
                    );
            }
            if (!CollectionUtils.isEmpty(fetchAttributes)) {
                specification = specification.and(SpecificationUtils.fetchAttributes(stockItemMapper.toEntityAttributes(fetchAttributes)));
            }
            specification = specification.and(SpecificationUtils.distinct(true));
        }
        return specification;
    }

    private StockItem toDomainModel(StockItemEntity stockItemEntity, Set<String> domainAttributes) {
        StockItem stockItem = stockItemMapper.toDomain(stockItemEntity);
        return enrichStockItemDomain(stockItem, stockItemEntity, domainAttributes);
    }

    private StockItem enrichStockItemDomain(StockItem stockItem, StockItemEntity stockItemEntity, Set<String> domainAttributes) {
        if (CollectionUtils.isEmpty(domainAttributes)) {
            return stockItem;
        }

        if (domainAttributes.contains(StockItemMapper.DOMAIN_STORE)) {
            stockItem.setStore(storeMapper.toDomain(stockItemEntity.getStore()));
        }
        if (domainAttributes.contains(StockItemMapper.DOMAIN_PRODUCT)) {
            stockItem.setProduct(productMapper.toDomain(stockItemEntity.getProduct()));
        }
        if (domainAttributes.contains(StockItemMapper.DOMAIN_OPTION_DETAIL)) {
            stockItem.setOptionDetail(optionDetailMapper.toDomain(stockItemEntity.getOptionDetail()));
        }
        if (domainAttributes.contains(StockItemMapper.DOMAIN_STOCK_BATCHES)) {
            stockItem.setStockBatches(
                stockItemEntity
                    .getStockBatches()
                    .stream()
                    .map(stockBatchEntity -> stockBatchMapper.toDomain(stockBatchEntity))
                    .collect(Collectors.toSet())
            );
        }
        return stockItem;
    }
}
