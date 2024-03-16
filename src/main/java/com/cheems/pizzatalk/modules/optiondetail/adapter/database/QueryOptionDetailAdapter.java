package com.cheems.pizzatalk.modules.optiondetail.adapter.database;

import com.cheems.pizzatalk.common.exception.AdapterException;
import com.cheems.pizzatalk.common.service.QueryService;
import com.cheems.pizzatalk.common.specification.SpecificationUtils;
import com.cheems.pizzatalk.entities.OptionDetailEntity;
import com.cheems.pizzatalk.entities.OptionDetailEntity_;
import com.cheems.pizzatalk.entities.OptionEntity_;
import com.cheems.pizzatalk.entities.StockItemEntity_;
import com.cheems.pizzatalk.entities.mapper.OptionDetailMapper;
import com.cheems.pizzatalk.entities.mapper.OptionMapper;
import com.cheems.pizzatalk.entities.mapper.StockItemMapper;
import com.cheems.pizzatalk.modules.optiondetail.application.port.in.query.OptionDetailCriteria;
import com.cheems.pizzatalk.modules.optiondetail.application.port.out.QueryOptionDetailPort;
import com.cheems.pizzatalk.modules.optiondetail.domain.OptionDetail;
import com.cheems.pizzatalk.repository.OptionDetailRepository;
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
public class QueryOptionDetailAdapter extends QueryService<OptionDetailEntity> implements QueryOptionDetailPort {

    private final OptionDetailRepository optionDetailRepository;

    private final OptionDetailMapper optionDetailMapper;

    private final OptionMapper optionMapper;

    private final StockItemMapper stockItemMapper;

    public QueryOptionDetailAdapter(
        OptionDetailRepository optionDetailRepository,
        OptionDetailMapper optionDetailMapper,
        OptionMapper optionMapper,
        StockItemMapper stockItemMapper
    ) {
        this.optionDetailRepository = optionDetailRepository;
        this.optionDetailMapper = optionDetailMapper;
        this.optionMapper = optionMapper;
        this.stockItemMapper = stockItemMapper;
    }

    @Override
    public Optional<OptionDetail> findByCriteria(OptionDetailCriteria criteria) {
        List<OptionDetailEntity> optionDetailEntities = optionDetailRepository.findAll(createSpecification(criteria));
        if (CollectionUtils.isEmpty(optionDetailEntities)) {
            return Optional.empty();
        }

        Integer optionDetailEntitiesSize = optionDetailEntities.size();
        if (optionDetailEntitiesSize > 1) {
            Set<Long> optionDetailIds = optionDetailEntities.stream().map(OptionDetailEntity::getId).collect(Collectors.toSet());
            throw new AdapterException("Found more than one option detail: " + optionDetailIds);
        }
        return Optional
            .of(optionDetailEntities.get(0))
            .map(optionDetailEntity -> toDomainModel(optionDetailEntity, criteria.getFetchAttributes()));
    }

    @Override
    public List<OptionDetail> findListByCriteria(OptionDetailCriteria criteria) {
        return optionDetailRepository
            .findAll(createSpecification(criteria))
            .stream()
            .map(optionDetailEntity -> toDomainModel(optionDetailEntity, criteria.getFetchAttributes()))
            .collect(Collectors.toList());
    }

    @Override
    public Page<OptionDetail> findPageByCriteria(OptionDetailCriteria criteria, Pageable pageable) {
        Page<OptionDetailEntity> optionDetailEntitiesPage = optionDetailRepository.findAll(createSpecification(criteria), pageable);
        return new PageImpl<>(
            optionDetailMapper.toDomain(optionDetailEntitiesPage.getContent()),
            pageable,
            optionDetailEntitiesPage.getTotalElements()
        );
    }

    private Specification<OptionDetailEntity> createSpecification(OptionDetailCriteria criteria) {
        Set<String> fetchAttributes = criteria.getFetchAttributes();
        Specification<OptionDetailEntity> specification = Specification.where(null);

        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OptionDetailEntity_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), OptionDetailEntity_.name));
            }
            if (criteria.getSku() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSku(), OptionDetailEntity_.sku));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), OptionDetailEntity_.code));
            }
            if (criteria.getUomId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUomId(), OptionDetailEntity_.uomId));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), OptionDetailEntity_.status));
            }
            if (criteria.getOptionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOptionId(),
                            root -> root.join(OptionDetailEntity_.option, JoinType.LEFT).get(OptionEntity_.id)
                        )
                    );
            }
            if (criteria.getStockItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStockItemId(),
                            root -> root.join(OptionDetailEntity_.stockItems, JoinType.LEFT).get(StockItemEntity_.id)
                        )
                    );
            }
            if (!CollectionUtils.isEmpty(fetchAttributes)) {
                specification =
                    specification.and(SpecificationUtils.fetchAttributes(optionDetailMapper.toEntityAttributes(fetchAttributes)));
            }
            specification = specification.and(SpecificationUtils.distinct(true));
        }
        return specification;
    }

    private OptionDetail toDomainModel(OptionDetailEntity optionDetailEntity, Set<String> domainAttributes) {
        OptionDetail optionDetail = optionDetailMapper.toDomain(optionDetailEntity);
        return enrichOptionDetailDomain(optionDetail, optionDetailEntity, domainAttributes);
    }

    private OptionDetail enrichOptionDetailDomain(
        OptionDetail optionDetail,
        OptionDetailEntity optionDetailEntity,
        Set<String> domainAttributes
    ) {
        if (CollectionUtils.isEmpty(domainAttributes)) {
            return optionDetail;
        }

        if (domainAttributes.contains(OptionDetailMapper.DOMAIN_OPTION)) {
            optionDetail.setOption(optionMapper.toDomain(optionDetailEntity.getOption()));
        }
        if (domainAttributes.contains(OptionDetailMapper.DOMAIN_STOCK_ITEMS)) {
            optionDetail.setStockItems(
                optionDetailEntity
                    .getStockItems()
                    .stream()
                    .map(stockItemEntity -> stockItemMapper.toDomain(stockItemEntity))
                    .collect(Collectors.toSet())
            );
        }
        return optionDetail;
    }
}
