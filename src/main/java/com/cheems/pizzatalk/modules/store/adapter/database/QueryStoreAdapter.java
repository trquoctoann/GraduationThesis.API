package com.cheems.pizzatalk.modules.store.adapter.database;

import com.cheems.pizzatalk.common.exception.AdapterException;
import com.cheems.pizzatalk.common.service.QueryService;
import com.cheems.pizzatalk.common.specification.SpecificationUtils;
import com.cheems.pizzatalk.entities.AreaEntity_;
import com.cheems.pizzatalk.entities.StoreEntity;
import com.cheems.pizzatalk.entities.StoreEntity_;
import com.cheems.pizzatalk.entities.mapper.AreaMapper;
import com.cheems.pizzatalk.entities.mapper.StockItemMapper;
import com.cheems.pizzatalk.entities.mapper.StoreMapper;
import com.cheems.pizzatalk.modules.store.application.port.in.query.StoreCriteria;
import com.cheems.pizzatalk.modules.store.application.port.out.QueryStorePort;
import com.cheems.pizzatalk.modules.store.domain.Store;
import com.cheems.pizzatalk.repository.StoreRepository;
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
public class QueryStoreAdapter extends QueryService<StoreEntity> implements QueryStorePort {

    private final StoreRepository storeRepository;

    private final StoreMapper storeMapper;

    private final AreaMapper areaMapper;

    private final StockItemMapper stockItemMapper;

    public QueryStoreAdapter(
        StoreRepository storeRepository,
        StoreMapper storeMapper,
        AreaMapper areaMapper,
        StockItemMapper stockItemMapper
    ) {
        this.storeRepository = storeRepository;
        this.storeMapper = storeMapper;
        this.areaMapper = areaMapper;
        this.stockItemMapper = stockItemMapper;
    }

    @Override
    public Optional<Store> findByCriteria(StoreCriteria criteria) {
        List<StoreEntity> storeEntities = storeRepository.findAll(createSpecification(criteria));
        if (CollectionUtils.isEmpty(storeEntities)) {
            return Optional.empty();
        }
        Integer storeEntitiesSize = storeEntities.size();
        if (storeEntitiesSize > 1) {
            Set<Long> storeIds = storeEntities.stream().map(StoreEntity::getId).collect(Collectors.toSet());
            throw new AdapterException("Found more than one store: " + storeIds);
        }
        return Optional.of(storeEntities.get(0)).map(storeEntity -> toDomainModel(storeEntity, criteria.getFetchAttributes()));
    }

    @Override
    public List<Store> findListByCriteria(StoreCriteria criteria) {
        return storeRepository
            .findAll(createSpecification(criteria))
            .stream()
            .map(storeEntity -> toDomainModel(storeEntity, criteria.getFetchAttributes()))
            .collect(Collectors.toList());
    }

    @Override
    public Page<Store> findPageByCriteria(StoreCriteria criteria, Pageable pageable) {
        Page<StoreEntity> storeEntitiesPage = storeRepository.findAll(createSpecification(criteria), pageable);
        return new PageImpl<>(storeMapper.toDomain(storeEntitiesPage.getContent()), pageable, storeEntitiesPage.getTotalElements());
    }

    private Specification<StoreEntity> createSpecification(StoreCriteria criteria) {
        Set<String> fetchAttributes = criteria.getFetchAttributes();
        Specification<StoreEntity> specification = Specification.where(null);

        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), StoreEntity_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), StoreEntity_.name));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), StoreEntity_.address));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), StoreEntity_.phoneNumber));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), StoreEntity_.email));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), StoreEntity_.status));
            }
            if (criteria.getAllowDelivery() != null) {
                specification = specification.and(buildSpecification(criteria.getAllowDelivery(), StoreEntity_.allowDelivery));
            }
            if (criteria.getAllowPickup() != null) {
                specification = specification.and(buildSpecification(criteria.getAllowPickup(), StoreEntity_.allowPickup));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), StoreEntity_.country));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildStringSpecification(criteria.getState(), StoreEntity_.state));
            }
            if (criteria.getDistrict() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDistrict(), StoreEntity_.district));
            }
            if (criteria.getLongitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLongitude(), StoreEntity_.longitude));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLatitude(), StoreEntity_.latitude));
            }
            if (criteria.getOpeningHour() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOpeningHour(), StoreEntity_.openingHour));
            }
            if (criteria.getImagePath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImagePath(), StoreEntity_.imagePath));
            }
            if (criteria.getAreaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAreaId(), root -> root.join(StoreEntity_.area, JoinType.LEFT).get(AreaEntity_.id))
                    );
            }
            if (!CollectionUtils.isEmpty(fetchAttributes)) {
                specification = specification.and(SpecificationUtils.fetchAttributes(storeMapper.toEntityAttributes(fetchAttributes)));
            }
            specification = specification.and(SpecificationUtils.distinct(true));
        }
        return specification;
    }

    private Store toDomainModel(StoreEntity storeEntity, Set<String> domainAttributes) {
        Store store = storeMapper.toDomain(storeEntity);
        return enrichStoreDomain(store, storeEntity, domainAttributes);
    }

    private Store enrichStoreDomain(Store store, StoreEntity storeEntity, Set<String> domainAttributes) {
        if (CollectionUtils.isEmpty(domainAttributes)) {
            return store;
        }

        if (domainAttributes.contains(StoreMapper.DOMAIN_AREA)) {
            store.setArea(areaMapper.toDomain(storeEntity.getArea()));
        }
        if (domainAttributes.contains(StoreMapper.DOMAIN_STOCK_ITEM)) {
            store.setStockItems(
                storeEntity
                    .getStockItems()
                    .stream()
                    .map(stockItemEntity -> stockItemMapper.toDomain(stockItemEntity))
                    .collect(Collectors.toSet())
            );
        }
        return store;
    }
}
