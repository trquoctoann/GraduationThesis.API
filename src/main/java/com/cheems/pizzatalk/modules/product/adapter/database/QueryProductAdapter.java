package com.cheems.pizzatalk.modules.product.adapter.database;

import com.cheems.pizzatalk.common.exception.AdapterException;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.service.QueryService;
import com.cheems.pizzatalk.common.specification.SpecificationUtils;
import com.cheems.pizzatalk.entities.CategoryEntity_;
import com.cheems.pizzatalk.entities.ProductEntity;
import com.cheems.pizzatalk.entities.ProductEntity_;
import com.cheems.pizzatalk.entities.ProductOptionEntity;
import com.cheems.pizzatalk.entities.ProductOptionEntity_;
import com.cheems.pizzatalk.entities.StockItemEntity;
import com.cheems.pizzatalk.entities.StockItemEntity_;
import com.cheems.pizzatalk.entities.mapper.CategoryMapper;
import com.cheems.pizzatalk.entities.mapper.OptionDetailMapper;
import com.cheems.pizzatalk.entities.mapper.OptionMapper;
import com.cheems.pizzatalk.entities.mapper.ProductMapper;
import com.cheems.pizzatalk.entities.mapper.StockItemMapper;
import com.cheems.pizzatalk.modules.product.application.port.in.query.ProductCriteria;
import com.cheems.pizzatalk.modules.product.application.port.out.QueryProductPort;
import com.cheems.pizzatalk.modules.product.domain.Product;
import com.cheems.pizzatalk.repository.ProductRepository;
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
public class QueryProductAdapter extends QueryService<ProductEntity> implements QueryProductPort {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final CategoryMapper categoryMapper;

    private final OptionMapper optionMapper;

    private final OptionDetailMapper optionDetailMapper;

    private final StockItemMapper stockItemMapper;

    public QueryProductAdapter(
        ProductRepository productRepository,
        ProductMapper productMapper,
        CategoryMapper categoryMapper,
        OptionMapper optionMapper,
        OptionDetailMapper optionDetailMapper,
        StockItemMapper stockItemMapper
    ) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryMapper = categoryMapper;
        this.optionMapper = optionMapper;
        this.optionDetailMapper = optionDetailMapper;
        this.stockItemMapper = stockItemMapper;
    }

    @Override
    public Optional<Product> findByCriteria(ProductCriteria criteria) {
        List<ProductEntity> productEntities = productRepository.findAll(createSpecification(criteria));
        if (CollectionUtils.isEmpty(productEntities)) {
            return Optional.empty();
        }
        Integer productEntitiesSize = productEntities.size();
        if (productEntitiesSize > 1) {
            Set<Long> productIds = productEntities.stream().map(ProductEntity::getId).collect(Collectors.toSet());
            throw new AdapterException("Found more than one product: " + productIds);
        }
        return Optional.of(productEntities.get(0)).map(productEntity -> toDomainModel(productEntity, criteria.getFetchAttributes()));
    }

    @Override
    public List<Product> findListByCriteria(ProductCriteria criteria) {
        return productRepository
            .findAll(createSpecification(criteria))
            .stream()
            .map(productEntity -> toDomainModel(productEntity, criteria.getFetchAttributes()))
            .collect(Collectors.toList());
    }

    @Override
    public Page<Product> findPageByCriteria(ProductCriteria criteria, Pageable pageable) {
        Page<ProductEntity> productEntitiesPage = productRepository.findAll(createSpecification(criteria), pageable);
        return new PageImpl<>(productMapper.toDomain(productEntitiesPage.getContent()), pageable, productEntitiesPage.getTotalElements());
    }

    private Specification<ProductEntity> createSpecification(ProductCriteria criteria) {
        Set<String> fetchAttributes = criteria.getFetchAttributes();
        Specification<ProductEntity> specification = Specification.where(null);

        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductEntity_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ProductEntity_.name));
            }
            if (criteria.getSize() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSize(), ProductEntity_.size));
            }
            if (criteria.getSlug() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlug(), ProductEntity_.slug));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ProductEntity_.description));
            }
            if (criteria.getSku() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSku(), ProductEntity_.sku));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), ProductEntity_.status));
            }
            if (criteria.getImagePath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImagePath(), ProductEntity_.imagePath));
            }
            if (criteria.getParentProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getParentProductId(),
                            root -> root.join(ProductEntity_.parentProduct, JoinType.LEFT).get(ProductEntity_.id)
                        )
                    );
            }
            if (criteria.getCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCategoryId(),
                            root -> root.join(ProductEntity_.category, JoinType.LEFT).get(CategoryEntity_.id)
                        )
                    );
            }
            if (criteria.getProductVariationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductVariationId(),
                            root -> root.join(ProductEntity_.productVariations, JoinType.LEFT).get(ProductEntity_.id)
                        )
                    );
            }
            if (criteria.getOptionId() != null) {
                specification = specification.and(buildSpecificationFindByOptionId(criteria.getOptionId()));
            }
            if (criteria.getStoreId() != null) {
                specification = specification.and(buildSpecificationFindByStoreId(criteria.getStoreId()));
            }
            if (!CollectionUtils.isEmpty(fetchAttributes)) {
                specification = specification.and(SpecificationUtils.fetchAttributes(productMapper.toEntityAttributes(fetchAttributes)));
            }
            specification = specification.and(SpecificationUtils.distinct(true));
        }
        return specification;
    }

    private Specification<ProductEntity> buildSpecificationFindByOptionId(RangeFilter<Long> optionId) {
        if (optionId.getEquals() != null) {
            return (root, query, builder) -> {
                Join<ProductEntity, ProductOptionEntity> joinProductOption = SpecificationUtils.getJoinFetch(
                    root,
                    "productOptions",
                    JoinType.LEFT,
                    false
                );
                return builder.equal(joinProductOption.get(ProductOptionEntity_.option), optionId.getEquals());
            };
        }
        return null;
    }

    private Specification<ProductEntity> buildSpecificationFindByStoreId(RangeFilter<Long> storeId) {
        if (storeId.getEquals() != null) {
            return (root, query, builder) -> {
                Join<ProductEntity, StockItemEntity> joinStockItem = SpecificationUtils.getJoinFetch(
                    root,
                    "stockItems",
                    JoinType.LEFT,
                    false
                );
                return builder.equal(joinStockItem.get(StockItemEntity_.store), storeId.getEquals());
            };
        }
        return null;
    }

    private Product toDomainModel(ProductEntity productEntity, Set<String> domainAttributes) {
        Product product = productMapper.toDomain(productEntity);
        return enrichProductDomain(product, productEntity, domainAttributes);
    }

    private Product enrichProductDomain(Product product, ProductEntity productEntity, Set<String> domainAttributes) {
        if (CollectionUtils.isEmpty(domainAttributes)) {
            return product;
        }

        if (domainAttributes.contains(ProductMapper.DOMAIN_PARENT_PRODUCT)) {
            product.setParentProduct(productMapper.toDomain(productEntity.getParentProduct()));
        }
        if (domainAttributes.contains(ProductMapper.DOMAIN_CATEGORY)) {
            product.setCategory(categoryMapper.toDomain(productEntity.getCategory()));
        }
        if (domainAttributes.contains(ProductMapper.DOMAIN_PRODUCT_VARIATIONS)) {
            product.setProductVariations(
                productEntity
                    .getProductVariations()
                    .stream()
                    .map(productTemp -> productMapper.toDomain(productTemp))
                    .collect(Collectors.toSet())
            );
        }
        if (domainAttributes.contains(ProductMapper.DOMAIN_OPTIONS)) {
            product.setOptions(
                productEntity
                    .getProductOptions()
                    .stream()
                    .map(productOptionEntity -> optionMapper.toDomain(productOptionEntity.getOption()))
                    .collect(Collectors.toSet())
            );
        }
        if (domainAttributes.contains(ProductMapper.DOMAIN_OPTION_DETAILS)) {
            product.setOptionDetails(
                productEntity
                    .getProductOptions()
                    .stream()
                    .flatMap(productOptionEntity -> productOptionEntity.getProductOptionDetails().stream())
                    .map(productOptionDetailEntity -> optionDetailMapper.toDomain(productOptionDetailEntity.getOptionDetail()))
                    .collect(Collectors.toSet())
            );
        }
        if (domainAttributes.contains(ProductMapper.DOMAIN_STOCK_ITEMS)) {
            product.setStockItems(
                productEntity
                    .getStockItems()
                    .stream()
                    .map(stockItemEntity -> stockItemMapper.toDomain(stockItemEntity))
                    .collect(Collectors.toSet())
            );
        }
        return product;
    }
}
