package com.cheems.pizzatalk.entities.mapper;

import com.cheems.pizzatalk.common.mapper.EntityMapper;
import com.cheems.pizzatalk.entities.StoreEntity;
import com.cheems.pizzatalk.modules.store.domain.Store;
import java.util.HashSet;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { AreaMapper.class })
public interface StoreMapper extends EntityMapper<Store, StoreEntity> {
    String DOMAIN_AREA = "area";
    String DOMAIN_STOCK_ITEM = "stockItems";

    String ENTITY_AREA = "area";
    String ENTITY_STOCK_ITEM = "stockItems";

    @Override
    @Mapping(target = "area", source = "areaId")
    @Mapping(target = "stockItems", ignore = true)
    StoreEntity toEntity(Store domain);

    @Override
    @Mapping(target = "areaId", source = "area.id")
    @Mapping(target = "area", ignore = true)
    @Mapping(target = "stockItems", ignore = true)
    Store toDomain(StoreEntity entity);

    // prettier-ignore
    default Set<String> toEntityAttributes(Set<String> domainAttributes) {
        Set<String> entityAttributes = new HashSet<>();
        domainAttributes.forEach(
            domainAttribute -> {
                if (domainAttribute.equals(DOMAIN_AREA)) {
                    entityAttributes.add(ENTITY_AREA);
                }
                if (domainAttribute.equals(DOMAIN_STOCK_ITEM)) {
                    entityAttributes.add(ENTITY_STOCK_ITEM);
                }
            }
        );
        return entityAttributes;
    }

    default StoreEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setId(id);
        return storeEntity;
    }
}
