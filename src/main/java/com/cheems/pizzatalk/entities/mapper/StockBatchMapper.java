package com.cheems.pizzatalk.entities.mapper;

import com.cheems.pizzatalk.common.mapper.EntityMapper;
import com.cheems.pizzatalk.entities.StockBatchEntity;
import com.cheems.pizzatalk.modules.stockbatch.domain.StockBatch;
import java.util.HashSet;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { StockItemMapper.class })
public interface StockBatchMapper extends EntityMapper<StockBatch, StockBatchEntity> {
    String DOMAIN_STOCK_ITEM = "stockItem";

    String ENTITY_STOCK_ITEM = "stockItem";

    @Override
    @Mapping(target = "stockItem", source = "stockItemId")
    StockBatchEntity toEntity(StockBatch domain);

    @Override
    @Mapping(target = "stockItemId", source = "stockItem.id")
    @Mapping(target = "stockItem", ignore = true)
    StockBatch toDomain(StockBatchEntity entity);

    // prettier-ignore
    @Override
    default Set<String> toEntityAttributes(Set<String> domainAttributes) {
        Set<String> entityAttributes = new HashSet<>();
        domainAttributes.forEach(
            domainAttribute -> {
                if (domainAttribute.equals(DOMAIN_STOCK_ITEM)) {
                    entityAttributes.add(ENTITY_STOCK_ITEM);
                }
            }
        );
        return entityAttributes;
    }

    default StockBatchEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        StockBatchEntity stockBatchEntity = new StockBatchEntity();
        stockBatchEntity.setId(id);
        return stockBatchEntity;
    }
}
