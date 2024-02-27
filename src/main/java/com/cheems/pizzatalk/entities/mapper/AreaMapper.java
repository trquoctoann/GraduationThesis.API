package com.cheems.pizzatalk.entities.mapper;

import com.cheems.pizzatalk.common.mapper.EntityMapper;
import com.cheems.pizzatalk.entities.AreaEntity;
import com.cheems.pizzatalk.modules.area.domain.Area;
import java.util.HashSet;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AreaMapper extends EntityMapper<Area, AreaEntity> {
    String DOMAIN_STORE = "stores";

    String ENTITY_STORE = "stores";

    @Override
    @Mapping(target = "stores", ignore = true)
    AreaEntity toEntity(Area domain);

    @Override
    @Mapping(target = "stores", ignore = true)
    Area toDomain(AreaEntity entity);

    // prettier-ignore
    default Set<String> toEntityAttributes(Set<String> domainAttributes) {
        Set<String> entityAttributes = new HashSet<>();
        domainAttributes.forEach(
            domainAttribute -> {
                if (domainAttribute.equals(DOMAIN_STORE)) {
                    entityAttributes.add(ENTITY_STORE);
                }
            });
        return entityAttributes;
    }

    default AreaEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        AreaEntity areaEntity = new AreaEntity();
        areaEntity.setId(id);
        return areaEntity;
    }
}
