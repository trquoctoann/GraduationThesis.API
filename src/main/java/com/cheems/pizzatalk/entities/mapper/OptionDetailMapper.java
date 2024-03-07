package com.cheems.pizzatalk.entities.mapper;

import com.cheems.pizzatalk.common.mapper.EntityMapper;
import com.cheems.pizzatalk.entities.OptionDetailEntity;
import com.cheems.pizzatalk.modules.optiondetail.domain.OptionDetail;
import java.util.HashSet;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { OptionMapper.class })
public interface OptionDetailMapper extends EntityMapper<OptionDetail, OptionDetailEntity> {
    String DOMAIN_OPTION = "option";

    String ENTITY_OPTION = "option";

    @Override
    @Mapping(target = "option", source = "optionId")
    OptionDetailEntity toEntity(OptionDetail domain);

    @Override
    @Mapping(target = "optionId", source = "option.id")
    @Mapping(target = "option", ignore = true)
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
