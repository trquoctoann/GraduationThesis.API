package com.cheems.pizzatalk.modules.category.adapter.database;

import com.cheems.pizzatalk.common.exception.AdapterException;
import com.cheems.pizzatalk.common.service.QueryService;
import com.cheems.pizzatalk.common.specification.SpecificationUtils;
import com.cheems.pizzatalk.entities.CategoryEntity;
import com.cheems.pizzatalk.entities.CategoryEntity_;
import com.cheems.pizzatalk.entities.ProductEntity_;
import com.cheems.pizzatalk.entities.mapper.CategoryMapper;
import com.cheems.pizzatalk.entities.mapper.ProductMapper;
import com.cheems.pizzatalk.modules.category.application.port.in.query.CategoryCriteria;
import com.cheems.pizzatalk.modules.category.application.port.out.QueryCategoryPort;
import com.cheems.pizzatalk.modules.category.domain.Category;
import com.cheems.pizzatalk.repository.CategoryRepository;
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
public class QueryCategoryAdapter extends QueryService<CategoryEntity> implements QueryCategoryPort {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private final ProductMapper productMapper;

    public QueryCategoryAdapter(CategoryRepository categoryRepository, CategoryMapper categoryMapper, ProductMapper productMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.productMapper = productMapper;
    }

    @Override
    public Optional<Category> findByCriteria(CategoryCriteria criteria) {
        List<CategoryEntity> categoryEntities = categoryRepository.findAll(createSpecification(criteria));
        if (CollectionUtils.isEmpty(categoryEntities)) {
            return Optional.empty();
        }

        Integer categoryEntitiesSize = categoryEntities.size();
        if (categoryEntitiesSize > 1) {
            Set<Long> categoryIds = categoryEntities.stream().map(CategoryEntity::getId).collect(Collectors.toSet());
            throw new AdapterException("Found more than one category: " + categoryIds);
        }
        return Optional.of(categoryEntities.get(0)).map(categoryEntity -> toDomainModel(categoryEntity, criteria.getFetchAttributes()));
    }

    @Override
    public List<Category> findListByCriteria(CategoryCriteria criteria) {
        return categoryRepository
            .findAll(createSpecification(criteria))
            .stream()
            .map(categoryEntity -> toDomainModel(categoryEntity, criteria.getFetchAttributes()))
            .collect(Collectors.toList());
    }

    @Override
    public Page<Category> findPageByCriteria(CategoryCriteria criteria, Pageable pageable) {
        Page<CategoryEntity> categoryEntitiesPage = categoryRepository.findAll(createSpecification(criteria), pageable);
        return new PageImpl<>(
            categoryMapper.toDomain(categoryEntitiesPage.getContent()),
            pageable,
            categoryEntitiesPage.getTotalElements()
        );
    }

    private Specification<CategoryEntity> createSpecification(CategoryCriteria criteria) {
        Set<String> fetchAttributes = criteria.getFetchAttributes();
        Specification<CategoryEntity> specification = Specification.where(null);

        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CategoryEntity_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CategoryEntity_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), CategoryEntity_.description));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), CategoryEntity_.status));
            }
            if (criteria.getImagePath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImagePath(), CategoryEntity_.imagePath));
            }
            if (criteria.getProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductId(),
                            root -> root.join(CategoryEntity_.products, JoinType.LEFT).get(ProductEntity_.id)
                        )
                    );
            }
            if (!CollectionUtils.isEmpty(fetchAttributes)) {
                specification = specification.and(SpecificationUtils.fetchAttributes(categoryMapper.toEntityAttributes(fetchAttributes)));
            }
            specification = specification.and(SpecificationUtils.distinct(true));
        }
        return specification;
    }

    private Category toDomainModel(CategoryEntity categoryEntity, Set<String> domainAttributes) {
        Category category = categoryMapper.toDomain(categoryEntity);
        return enrichCategoryDomain(category, categoryEntity, domainAttributes);
    }

    private Category enrichCategoryDomain(Category category, CategoryEntity categoryEntity, Set<String> domainAttributes) {
        if (CollectionUtils.isEmpty(domainAttributes)) {
            return category;
        }

        if (domainAttributes.contains(CategoryMapper.DOMAIN_PRODUCT)) {
            category.setProducts(
                categoryEntity.getProducts().stream().map(product -> productMapper.toDomain(product)).collect(Collectors.toSet())
            );
        }
        return category;
    }
}
