package com.cheems.pizzatalk.entities.mapper;

import com.cheems.pizzatalk.common.mapper.EntityMapper;
import com.cheems.pizzatalk.entities.CategoryEntity;
import com.cheems.pizzatalk.modules.category.domain.Category;
import java.util.HashSet;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper extends EntityMapper<Category, CategoryEntity> {
    String DOMAIN_PRODUCT = "products";

    String ENTITY_PRODUCT = "products";

    @Override
    @Mapping(target = "products", ignore = true)
    CategoryEntity toEntity(Category domain);

    @Override
    @Mapping(target = "products", ignore = true)
    Category toDomain(CategoryEntity entity);

    // prettier-ignore
    @Override
    default Set<String> toEntityAttributes(Set<String> domainAttributes) {
        Set<String> entityAttributes = new HashSet<>();
        domainAttributes.forEach(
            domainAttribute -> {
                if (domainAttribute.equals(DOMAIN_PRODUCT)) {
                    entityAttributes.add(ENTITY_PRODUCT);
                }
            });
        return entityAttributes;
    }

    default CategoryEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(id);
        return categoryEntity;
    }
}
