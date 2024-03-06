package com.cheems.pizzatalk.modules.option.adapter.database;

import com.cheems.pizzatalk.common.exception.AdapterException;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.service.QueryService;
import com.cheems.pizzatalk.common.specification.SpecificationUtils;
import com.cheems.pizzatalk.entities.OptionDetailEntity_;
import com.cheems.pizzatalk.entities.OptionEntity;
import com.cheems.pizzatalk.entities.OptionEntity_;
import com.cheems.pizzatalk.entities.ProductOptionEntity;
import com.cheems.pizzatalk.entities.ProductOptionEntity_;
import com.cheems.pizzatalk.entities.mapper.OptionDetailMapper;
import com.cheems.pizzatalk.entities.mapper.OptionMapper;
import com.cheems.pizzatalk.entities.mapper.ProductMapper;
import com.cheems.pizzatalk.modules.option.application.port.in.query.OptionCriteria;
import com.cheems.pizzatalk.modules.option.application.port.out.QueryOptionPort;
import com.cheems.pizzatalk.modules.option.domain.Option;
import com.cheems.pizzatalk.repository.OptionRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class QueryOptionAdapter extends QueryService<OptionEntity> implements QueryOptionPort {

    private final OptionRepository optionRepository;

    private final OptionMapper optionMapper;

    private final ProductMapper productMapper;

    private final OptionDetailMapper optionDetailMapper;

    public QueryOptionAdapter(
        OptionRepository optionRepository,
        OptionMapper optionMapper,
        ProductMapper productMapper,
        OptionDetailMapper optionDetailMapper
    ) {
        this.optionRepository = optionRepository;
        this.optionMapper = optionMapper;
        this.productMapper = productMapper;
        this.optionDetailMapper = optionDetailMapper;
    }

    @Override
    public Optional<Option> findByCriteria(OptionCriteria criteria) {
        List<OptionEntity> optionEntities = optionRepository.findAll(createSpecification(criteria));
        if (CollectionUtils.isEmpty(optionEntities)) {
            return Optional.empty();
        }

        Integer sizeOptionEntities = optionEntities.size();
        if (sizeOptionEntities > 1) {
            Set<Long> optionIds = optionEntities.stream().map(OptionEntity::getId).collect(Collectors.toSet());
            throw new AdapterException("Found more than one option: " + optionIds);
        }
        return Optional.of(optionEntities.get(0)).map(optionEntity -> toDomainModel(optionEntity, criteria.getFetchAttributes()));
    }

    @Override
    public List<Option> findListByCriteria(OptionCriteria criteria) {
        return optionRepository
            .findAll(createSpecification(criteria))
            .stream()
            .map(optionEntity -> toDomainModel(optionEntity, criteria.getFetchAttributes()))
            .collect(Collectors.toList());
    }

    @Override
    public Page<Option> findPageByCriteria(OptionCriteria criteria, Pageable pageable) {
        Page<OptionEntity> optionEntitiesPage = optionRepository.findAll(createSpecification(criteria), pageable);
        return new PageImpl<>(optionMapper.toDomain(optionEntitiesPage.getContent()), pageable, optionEntitiesPage.getTotalElements());
    }

    private Specification<OptionEntity> createSpecification(OptionCriteria criteria) {
        Set<String> fetchAttributes = criteria.getFetchAttributes();
        Specification<OptionEntity> specification = Specification.where(null);

        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OptionEntity_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), OptionEntity_.code));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), OptionEntity_.status));
            }
            if (criteria.getIsMulti() != null) {
                specification = specification.and(buildSpecification(criteria.getIsMulti(), OptionEntity_.isMulti));
            }
            if (criteria.getIsRequired() != null) {
                specification = specification.and(buildSpecification(criteria.getIsRequired(), OptionEntity_.isRequired));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(buildSpecificationFindByProductId(criteria.getProductId()));
            }
            if (criteria.getOptionDetailId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOptionDetailId(),
                            root -> root.join(OptionEntity_.optionDetails, JoinType.LEFT).get(OptionDetailEntity_.id)
                        )
                    );
            }
            if (!CollectionUtils.isEmpty(fetchAttributes)) {
                specification = specification.and(SpecificationUtils.fetchAttributes(optionMapper.toEntityAttributes(fetchAttributes)));
            }
            specification = specification.and(SpecificationUtils.distinct(true));
        }
        return specification;
    }

    private Specification<OptionEntity> buildSpecificationFindByProductId(RangeFilter<Long> productId) {
        if (productId.getEquals() != null) {
            return (root, query, builder) -> {
                Join<OptionEntity, ProductOptionEntity> joinProductOption = SpecificationUtils.getJoinFetch(
                    root,
                    "productOptions",
                    JoinType.LEFT,
                    false
                );
                return builder.equal(joinProductOption.get(ProductOptionEntity_.product), productId.getEquals());
            };
        }
        return null;
    }

    private Option toDomainModel(OptionEntity optionDetail, Set<String> domainAttributes) {
        Option option = optionMapper.toDomain(optionDetail);
        return enrichOptionDomain(option, optionDetail, domainAttributes);
    }

    private Option enrichOptionDomain(Option option, OptionEntity optionEntity, Set<String> domainAttributes) {
        if (CollectionUtils.isEmpty(domainAttributes)) {
            return option;
        }

        if (domainAttributes.contains(OptionMapper.DOMAIN_PRODUCTS)) {
            option.setProducts(
                optionEntity
                    .getProductOptions()
                    .stream()
                    .map(productOptionEntity -> productMapper.toDomain(productOptionEntity.getProduct()))
                    .collect(Collectors.toSet())
            );
        }
        if (domainAttributes.contains(OptionMapper.DOMAIN_OPTION_DETAILS)) {
            option.setOptionDetails(
                optionEntity
                    .getOptionDetails()
                    .stream()
                    .map(optionDetail -> optionDetailMapper.toDomain(optionDetail))
                    .collect(Collectors.toSet())
            );
        }
        return option;
    }
}
