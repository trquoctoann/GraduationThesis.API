package com.cheems.pizzatalk.entities.mapper;

import com.cheems.pizzatalk.common.mapper.EntityMapper;
import com.cheems.pizzatalk.entities.OptionDetailEntity;
import com.cheems.pizzatalk.modules.optiondetail.domain.OptionDetail;
import java.util.HashSet;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = { OptionMapper.class }, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OptionDetailMapper extends EntityMapper<OptionDetail, OptionDetailEntity> {
    String DOMAIN_OPTION = "option";
    String DOMAIN_STOCK_ITEMS = "stockItems";

    String ENTITY_OPTION = "option";
    String ENTITY_STOCK_ITEMS = "stockItems";

    @Override
    @Mapping(target = "option", source = "optionId")
    @Mapping(target = "stockItems", ignore = true)
    OptionDetailEntity toEntity(OptionDetail domain);

    @Override
    @Mapping(target = "optionId", source = "option.id")
    @Mapping(target = "option", ignore = true)
    @Mapping(target = "stockItems", ignore = true)
    OptionDetail toDomain(OptionDetailEntity entity);

    // prettier-ignore
    @Override
    default Set<String> toEntityAttributes(Set<String> domainAttributes) {
        Set<String> entityAttributes = new HashSet<>();
        domainAttributes.forEach(
            domainAttribute -> {
                if (domainAttribute.equals(DOMAIN_OPTION)) {
                    entityAttributes.add(ENTITY_OPTION);
                }
                if (domainAttribute.equals(DOMAIN_STOCK_ITEMS)) {
                    entityAttributes.add(ENTITY_STOCK_ITEMS);
                }
            });
        return entityAttributes;
    }

    default OptionDetailEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        OptionDetailEntity optionDetailEntity = new OptionDetailEntity();
        optionDetailEntity.setId(id);
        return optionDetailEntity;
    }
}
