package com.cheems.pizzatalk.modules.deal.adapter.database;

import com.cheems.pizzatalk.common.exception.AdapterException;
import com.cheems.pizzatalk.common.service.QueryService;
import com.cheems.pizzatalk.common.specification.SpecificationUtils;
import com.cheems.pizzatalk.entities.DealEntity;
import com.cheems.pizzatalk.entities.DealEntity_;
import com.cheems.pizzatalk.entities.mapper.DealMapper;
import com.cheems.pizzatalk.modules.deal.application.port.in.query.DealCriteria;
import com.cheems.pizzatalk.modules.deal.application.port.out.QueryDealPort;
import com.cheems.pizzatalk.modules.deal.domain.Deal;
import com.cheems.pizzatalk.repository.DealRepository;
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
public class QueryDealAdapter extends QueryService<DealEntity> implements QueryDealPort {

    private final DealRepository dealRepository;

    private final DealMapper dealMapper;

    public QueryDealAdapter(DealRepository dealRepository, DealMapper dealMapper) {
        this.dealRepository = dealRepository;
        this.dealMapper = dealMapper;
    }

    @Override
    public Optional<Deal> findByCriteria(DealCriteria criteria) {
        List<DealEntity> dealEntities = dealRepository.findAll(createSpecification(criteria));
        if (CollectionUtils.isEmpty(dealEntities)) {
            return Optional.empty();
        }

        Integer dealEntitiesSize = dealEntities.size();
        if (dealEntitiesSize > 1) {
            Set<Long> dealIds = dealEntities.stream().map(DealEntity::getId).collect(Collectors.toSet());
            throw new AdapterException("Found more than one deal: " + dealIds);
        }
        return Optional.of(dealEntities.get(0)).map(dealEntity -> toDomainModel(dealEntity, criteria.getFetchAttributes()));
    }

    @Override
    public List<Deal> findListByCriteria(DealCriteria criteria) {
        return dealRepository
            .findAll(createSpecification(criteria))
            .stream()
            .map(dealEntity -> toDomainModel(dealEntity, criteria.getFetchAttributes()))
            .collect(Collectors.toList());
    }

    @Override
    public Page<Deal> findPageByCriteria(DealCriteria criteria, Pageable pageable) {
        Page<DealEntity> dealEntitiesPage = dealRepository.findAll(createSpecification(criteria), pageable);
        return new PageImpl<>(dealMapper.toDomain(dealEntitiesPage.getContent()), pageable, dealEntitiesPage.getTotalElements());
    }

    private Specification<DealEntity> createSpecification(DealCriteria criteria) {
        Set<String> fetchAttributes = criteria.getFetchAttributes();
        Specification<DealEntity> specification = Specification.where(null);

        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DealEntity_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), DealEntity_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), DealEntity_.description));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), DealEntity_.status));
            }
            if (criteria.getDealNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDealNo(), DealEntity_.dealNo));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), DealEntity_.price));
            }
            if (criteria.getSlug() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlug(), DealEntity_.slug));
            }
            if (criteria.getImagePath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImagePath(), DealEntity_.imagePath));
            }
            if (criteria.getParentDealId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getParentDealId(),
                            root -> root.join(DealEntity_.parentDeal, JoinType.LEFT).get(DealEntity_.id)
                        )
                    );
            }
            if (criteria.getDealVariationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDealVariationId(),
                            root -> root.join(DealEntity_.dealVariations, JoinType.LEFT).get(DealEntity_.id)
                        )
                    );
            }
            if (!CollectionUtils.isEmpty(fetchAttributes)) {
                specification = specification.and(SpecificationUtils.fetchAttributes(dealMapper.toEntityAttributes(fetchAttributes)));
            }
            specification = specification.and(SpecificationUtils.distinct(true));
        }
        return specification;
    }

    private Deal toDomainModel(DealEntity dealEntity, Set<String> domainAttributes) {
        Deal deal = dealMapper.toDomain(dealEntity);
        return enrichDealDomain(deal, dealEntity, domainAttributes);
    }

    private Deal enrichDealDomain(Deal deal, DealEntity dealEntity, Set<String> domainAttributes) {
        if (CollectionUtils.isEmpty(domainAttributes)) {
            return deal;
        }

        if (domainAttributes.contains(DealMapper.DOMAIN_PARENT_DEAL)) {
            deal.setParentDeal(dealMapper.toDomain(dealEntity.getParentDeal()));
        }
        if (domainAttributes.contains(DealMapper.DOMAIN_DEAL_VARIATION)) {
            deal.setDealVariations(
                dealEntity.getDealVariations().stream().map(dealVariation -> dealMapper.toDomain(dealVariation)).collect(Collectors.toSet())
            );
        }
        return deal;
    }
}
