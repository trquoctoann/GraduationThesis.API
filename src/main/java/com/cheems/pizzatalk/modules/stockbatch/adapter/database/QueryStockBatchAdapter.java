package com.cheems.pizzatalk.modules.stockbatch.adapter.database;

import com.cheems.pizzatalk.common.exception.AdapterException;
import com.cheems.pizzatalk.common.service.QueryService;
import com.cheems.pizzatalk.common.specification.SpecificationUtils;
import com.cheems.pizzatalk.entities.StockBatchEntity;
import com.cheems.pizzatalk.entities.StockBatchEntity_;
import com.cheems.pizzatalk.entities.StockItemEntity_;
import com.cheems.pizzatalk.entities.mapper.StockBatchMapper;
import com.cheems.pizzatalk.entities.mapper.StockItemMapper;
import com.cheems.pizzatalk.modules.stockbatch.application.port.in.query.StockBatchCriteria;
import com.cheems.pizzatalk.modules.stockbatch.application.port.out.QueryStockBatchPort;
import com.cheems.pizzatalk.modules.stockbatch.domain.StockBatch;
import com.cheems.pizzatalk.repository.StockBatchRepository;
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
public class QueryStockBatchAdapter extends QueryService<StockBatchEntity> implements QueryStockBatchPort {

    private final StockBatchRepository stockBatchRepository;

    private final StockBatchMapper stockBatchMapper;

    private final StockItemMapper stockItemMapper;

    public QueryStockBatchAdapter(
        StockBatchRepository stockBatchRepository,
        StockBatchMapper stockBatchMapper,
        StockItemMapper stockItemMapper
    ) {
        this.stockBatchRepository = stockBatchRepository;
        this.stockBatchMapper = stockBatchMapper;
        this.stockItemMapper = stockItemMapper;
    }

    @Override
    public Optional<StockBatch> findByCriteria(StockBatchCriteria criteria) {
        List<StockBatchEntity> stockBatchEntities = stockBatchRepository.findAll(createSpecification(criteria));
        if (CollectionUtils.isEmpty(stockBatchEntities)) {
            return Optional.empty();
        }

        Integer stockBatchEntitiesSize = stockBatchEntities.size();
        if (stockBatchEntitiesSize > 1) {
            Set<Long> stockBatchIds = stockBatchEntities.stream().map(StockBatchEntity::getId).collect(Collectors.toSet());
            throw new AdapterException("Found more than one stock batch: " + stockBatchIds);
        }
        return Optional
            .of(stockBatchEntities.get(0))
            .map(stockBatchEntity -> toDomainModel(stockBatchEntity, criteria.getFetchAttributes()));
    }

    @Override
    public List<StockBatch> findListByCriteria(StockBatchCriteria criteria) {
        return stockBatchRepository
            .findAll(createSpecification(criteria))
            .stream()
            .map(stockBatchEntity -> toDomainModel(stockBatchEntity, criteria.getFetchAttributes()))
            .collect(Collectors.toList());
    }

    @Override
    public Page<StockBatch> findPageByCriteria(StockBatchCriteria criteria, Pageable pageable) {
        Page<StockBatchEntity> stockBatchEntitiesPage = stockBatchRepository.findAll(createSpecification(criteria), pageable);
        return new PageImpl<>(
            stockBatchMapper.toDomain(stockBatchEntitiesPage.getContent()),
            pageable,
            stockBatchEntitiesPage.getTotalElements()
        );
    }

    private Specification<StockBatchEntity> createSpecification(StockBatchCriteria criteria) {
        Set<String> fetchAttributes = criteria.getFetchAttributes();
        Specification<StockBatchEntity> specification = Specification.where(null);

        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), StockBatchEntity_.id));
            }
            if (criteria.getUnit() != null) {
                specification = specification.and(buildSpecification(criteria.getUnit(), StockBatchEntity_.unit));
            }
            if (criteria.getPurchasePricePerUnit() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPurchasePricePerUnit(), StockBatchEntity_.purchasePricePerUnit));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), StockBatchEntity_.status));
            }
            if (criteria.getRemainingQuantity() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getRemainingQuantity(), StockBatchEntity_.remainingQuantity));
            }
            if (criteria.getOrderedQuantity() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getOrderedQuantity(), StockBatchEntity_.orderedQuantity));
            }
            if (criteria.getReceivedQuantity() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getReceivedQuantity(), StockBatchEntity_.receivedQuantity));
            }
            if (criteria.getOrderedDate() != null) {
                specification = specification.and(buildSpecification(criteria.getOrderedDate(), StockBatchEntity_.orderedDate));
            }
            if (criteria.getReceivedDate() != null) {
                specification = specification.and(buildSpecification(criteria.getReceivedDate(), StockBatchEntity_.receivedDate));
            }
            if (criteria.getExpirationDate() != null) {
                specification = specification.and(buildSpecification(criteria.getExpirationDate(), StockBatchEntity_.expirationDate));
            }
            if (criteria.getStockItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStockItemId(),
                            root -> root.join(StockBatchEntity_.stockItem, JoinType.LEFT).get(StockItemEntity_.id)
                        )
                    );
            }
            if (!CollectionUtils.isEmpty(fetchAttributes)) {
                specification = specification.and(SpecificationUtils.fetchAttributes(stockBatchMapper.toEntityAttributes(fetchAttributes)));
            }
            specification = specification.and(SpecificationUtils.distinct(true));
        }
        return specification;
    }

    private StockBatch toDomainModel(StockBatchEntity stockBatchEntity, Set<String> domainAttributes) {
        StockBatch stockBatch = stockBatchMapper.toDomain(stockBatchEntity);
        return enrichStockBatchDomain(stockBatch, stockBatchEntity, domainAttributes);
    }

    private StockBatch enrichStockBatchDomain(StockBatch stockBatch, StockBatchEntity stockBatchEntity, Set<String> domainAttributes) {
        if (CollectionUtils.isEmpty(domainAttributes)) {
            return stockBatch;
        }

        if (domainAttributes.contains(StockBatchMapper.DOMAIN_STOCK_ITEM)) {
            stockBatch.setStockItem(stockItemMapper.toDomain(stockBatchEntity.getStockItem()));
        }
        return stockBatch;
    }
}
