package com.cheems.pizzatalk.modules.area.adapter.database;

import com.cheems.pizzatalk.common.exception.AdapterException;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.service.QueryService;
import com.cheems.pizzatalk.common.specification.SpecificationUtils;
import com.cheems.pizzatalk.entities.AreaEntity;
import com.cheems.pizzatalk.entities.AreaEntity_;
import com.cheems.pizzatalk.entities.StoreEntity_;
import com.cheems.pizzatalk.entities.mapper.AreaMapper;
import com.cheems.pizzatalk.entities.mapper.StoreMapper;
import com.cheems.pizzatalk.modules.area.application.port.in.query.AreaCriteria;
import com.cheems.pizzatalk.modules.area.application.port.out.QueryAreaPort;
import com.cheems.pizzatalk.modules.area.domain.Area;
import com.cheems.pizzatalk.repository.AreaRepository;
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
public class QueryAreaAdapter extends QueryService<AreaEntity> implements QueryAreaPort {

    private final AreaRepository areaRepository;

    private final AreaMapper areaMapper;

    private final StoreMapper storeMapper;

    public QueryAreaAdapter(AreaRepository areaRepository, AreaMapper areaMapper, StoreMapper storeMapper) {
        this.areaRepository = areaRepository;
        this.areaMapper = areaMapper;
        this.storeMapper = storeMapper;
    }

    @Override
    public Optional<Area> findByCriteria(AreaCriteria criteria) {
        List<AreaEntity> areaEntities = areaRepository.findAll(createSpecification(criteria));
        if (CollectionUtils.isEmpty(areaEntities)) {
            return Optional.empty();
        }
        Integer areaEntitiesSize = areaEntities.size();
        if (areaEntitiesSize > 1) {
            Set<Long> areaIds = areaEntities.stream().map(AreaEntity::getId).collect(Collectors.toSet());
            throw new AdapterException("Found more than one area: " + areaIds);
        }
        return Optional.of(areaEntities.get(0)).map(areaEntity -> toDomainModel(areaEntity, criteria.getFetchAttributes()));
    }

    @Override
    public List<Area> findListByCriteria(AreaCriteria criteria) {
        return areaRepository
            .findAll(createSpecification(criteria))
            .stream()
            .map(areaEntity -> toDomainModel(areaEntity, criteria.getFetchAttributes()))
            .collect(Collectors.toList());
    }

    @Override
    public Page<Area> findPageByCriteria(AreaCriteria criteria, Pageable pageable) {
        Page<AreaEntity> areaEntitiesPage = areaRepository.findAll(createSpecification(criteria), pageable);
        return new PageImpl<>(areaMapper.toDomain(areaEntitiesPage.getContent()), pageable, areaEntitiesPage.getTotalElements());
    }

    private Specification<AreaEntity> createSpecification(AreaCriteria criteria) {
        Set<String> fetchAttributes = criteria.getFetchAttributes();
        Specification<AreaEntity> specification = Specification.where(null);

        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AreaEntity_.id));
            }
            if (criteria.getOriginalId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOriginalId(), AreaEntity_.originalId));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AreaEntity_.name));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), AreaEntity_.code));
            }
            if (criteria.getBrandCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBrandCode(), AreaEntity_.brandCode));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), AreaEntity_.status));
            }
            if (criteria.getStoreCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStoreCount(), AreaEntity_.storeCount));
            }
            if (criteria.getPriceGroupId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPriceGroupId(), AreaEntity_.priceGroupId));
            }
            if (criteria.getStoreId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getStoreId(), root -> root.join(AreaEntity_.stores, JoinType.LEFT).get(StoreEntity_.id))
                    );
            }
            if (!CollectionUtils.isEmpty(fetchAttributes)) {
                specification = specification.and(SpecificationUtils.fetchAttributes(areaMapper.toEntityAttributes(fetchAttributes)));
            }
            specification = specification.and(SpecificationUtils.distinct(true));
        }
        return specification;
    }

    private Area toDomainModel(AreaEntity areaEntity, Set<String> domainAttributes) {
        Area area = areaMapper.toDomain(areaEntity);
        return enrichAreaDomain(area, areaEntity, domainAttributes);
    }

    private Area enrichAreaDomain(Area area, AreaEntity areaEntity, Set<String> domainAttributes) {
        if (CollectionUtils.isEmpty(domainAttributes)) {
            return area;
        }

        if (domainAttributes.contains(AreaMapper.DOMAIN_STORE)) {
            area.setStores(areaEntity.getStores().stream().map(store -> storeMapper.toDomain(store)).collect(Collectors.toSet()));
        }
        return area;
    }
}
