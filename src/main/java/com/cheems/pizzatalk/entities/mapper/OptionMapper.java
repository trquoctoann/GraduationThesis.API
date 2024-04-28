package com.cheems.pizzatalk.entities.mapper;

import com.cheems.pizzatalk.common.mapper.EntityMapper;
import com.cheems.pizzatalk.entities.OptionEntity;
import com.cheems.pizzatalk.modules.option.domain.Option;
import java.util.HashSet;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OptionMapper extends EntityMapper<Option, OptionEntity> {
    String DOMAIN_PRODUCTS = "products";
    String DOMAIN_OPTION_DETAILS = "optionDetails";

    String ENTITY_PRODUCTS = "productOptions.product";
    String ENTITY_OPTION_DETAILS = "optionDetails";

    @Override
    @Mapping(target = "optionDetails", ignore = true)
    OptionEntity toEntity(Option domain);

    @Override
    @Mapping(target = "optionDetails", ignore = true)
    Option toDomain(OptionEntity entity);

    // prettier-ignore
    @Override
    default Set<String> toEntityAttributes(Set<String> domainAttributes) {
        Set<String> entityAttributes = new HashSet<>();
        domainAttributes.forEach(
            domainAttribute -> {
                if (domainAttribute.equals(DOMAIN_PRODUCTS)) {
                    entityAttributes.add(ENTITY_PRODUCTS);
                }
                if (domainAttribute.equals(DOMAIN_OPTION_DETAILS)) {
                    entityAttributes.add(ENTITY_OPTION_DETAILS);
                }
            });
        return entityAttributes;
    }

    default OptionEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        OptionEntity optionEntity = new OptionEntity();
        optionEntity.setId(id);
        return optionEntity;
    }
}
