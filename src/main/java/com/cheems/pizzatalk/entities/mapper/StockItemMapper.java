package com.cheems.pizzatalk.entities.mapper;

import com.cheems.pizzatalk.common.mapper.EntityMapper;
import com.cheems.pizzatalk.entities.StockItemEntity;
import com.cheems.pizzatalk.modules.stockitem.domain.StockItem;
import java.util.HashSet;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    uses = { StoreMapper.class, ProductMapper.class, OptionDetailMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface StockItemMapper extends EntityMapper<StockItem, StockItemEntity> {
    String DOMAIN_STORE = "store";
    String DOMAIN_PRODUCT = "product";
    String DOMAIN_OPTION_DETAIL = "optionDetail";
    String DOMAIN_STOCK_BATCHES = "stockBatches";

    String ENTITY_STORE = "store";
    String ENTITY_PRODUCT = "product";
    String ENTITY_OPTION_DETAIL = "optionDetail";
    String ENTITY_STOCK_BATCHES = "stockBatches";

    @Override
    @Mapping(target = "store", source = "storeId")
    @Mapping(target = "product", source = "productId")
    @Mapping(target = "optionDetail", source = "optionDetailId")
    @Mapping(target = "stockBatches", ignore = true)
    StockItemEntity toEntity(StockItem domain);

    @Override
    @Mapping(target = "storeId", source = "store.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "optionDetailId", source = "optionDetail.id")
    @Mapping(target = "store", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "optionDetail", ignore = true)
    @Mapping(target = "stockBatches", ignore = true)
    StockItem toDomain(StockItemEntity entity);

    // prettier-ignore
    @Override
    default Set<String> toEntityAttributes(Set<String> domainAttributes) {
        Set<String> entityAttributes = new HashSet<>();
        domainAttributes.forEach(
            domainAttribute -> {
                if (domainAttribute.equals(DOMAIN_STORE)) {
                    entityAttributes.add(ENTITY_STORE);
                }
                if (domainAttribute.equals(DOMAIN_PRODUCT)) {
                    entityAttributes.add(ENTITY_PRODUCT);
                }
                if (domainAttribute.equals(DOMAIN_OPTION_DETAIL)) {
                    entityAttributes.add(ENTITY_OPTION_DETAIL);
                }
                if (domainAttribute.equals(DOMAIN_STOCK_BATCHES)) {
                    entityAttributes.add(ENTITY_STOCK_BATCHES);
                }
            }
        );
        return entityAttributes;
    }

    default StockItemEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        StockItemEntity stockItemEntity = new StockItemEntity();
        stockItemEntity.setId(id);
        return stockItemEntity;
    }
}
