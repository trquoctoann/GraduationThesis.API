package com.cheems.pizzatalk.entities.mapper;

import com.cheems.pizzatalk.common.mapper.EntityMapper;
import com.cheems.pizzatalk.entities.ProductEntity;
import com.cheems.pizzatalk.modules.product.domain.Product;
import java.util.HashSet;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { CategoryMapper.class })
public interface ProductMapper extends EntityMapper<Product, ProductEntity> {
    String DOMAIN_PARENT_PRODUCT = "parentProduct";
    String DOMAIN_CATEGORY = "category";
    String DOMAIN_PRODUCT_VARIATIONS = "productVariations";
    String DOMAIN_OPTIONS = "options";
    String DOMAIN_OPTION_DETAILS = "optionDetails";
    String DOMAIN_STOCK_ITEMS = "stockItems";

    String ENTITY_PARENT_PRODUCT = "parentProduct";
    String ENTITY_CATEGORY = "category";
    String ENTITY_PRODUCT_VARIATIONS = "productVariations";
    String ENTITY_OPTIONS = "productOptions.option";
    String ENTITY_OPTION_DETAILS = "productOptions.optionDetail";
    String ENTITY_STOCK_ITEMS = "stockItems";

    @Override
    @Mapping(target = "parentProduct", source = "parentProductId")
    @Mapping(target = "category", source = "categoryId")
    @Mapping(target = "productVariations", ignore = true)
    @Mapping(target = "stockItems", ignore = true)
    ProductEntity toEntity(Product domain);

    @Override
    @Mapping(target = "parentProductId", source = "parentProduct.id")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "parentProduct", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "productVariations", ignore = true)
    @Mapping(target = "stockItems", ignore = true)
    Product toDomain(ProductEntity entity);

    // prettier-ignore
    @Override
    default Set<String> toEntityAttributes(Set<String> domainAttributes) {
        Set<String> entityAttributes = new HashSet<>();
        domainAttributes.forEach(
            domainAttribute -> {
                if (domainAttribute.equals(DOMAIN_PARENT_PRODUCT)) {
                    entityAttributes.add(ENTITY_PARENT_PRODUCT);
                }
                if (domainAttribute.equals(DOMAIN_CATEGORY)) {
                    entityAttributes.add(ENTITY_CATEGORY);
                }
                if (domainAttribute.equals(DOMAIN_PRODUCT_VARIATIONS)) {
                    entityAttributes.add(ENTITY_PRODUCT_VARIATIONS);
                } 
                if (domainAttribute.equals(DOMAIN_OPTIONS)) {
                    entityAttributes.add(ENTITY_OPTIONS);
                }
                if (domainAttribute.equals(DOMAIN_OPTION_DETAILS)) {
                    entityAttributes.add(ENTITY_OPTION_DETAILS);
                }
                if (domainAttribute.equals(DOMAIN_STOCK_ITEMS)) {
                    entityAttributes.add(ENTITY_STOCK_ITEMS);
                }
            });
        return entityAttributes;
    }

    default ProductEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(id);
        return productEntity;
    }
}
