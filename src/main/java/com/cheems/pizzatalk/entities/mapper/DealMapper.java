package com.cheems.pizzatalk.entities.mapper;

import com.cheems.pizzatalk.common.mapper.EntityMapper;
import com.cheems.pizzatalk.entities.DealEntity;
import com.cheems.pizzatalk.modules.deal.domain.Deal;
import java.util.HashSet;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DealMapper extends EntityMapper<Deal, DealEntity> {
    String DOMAIN_PARENT_DEAL = "parentDeal";
    String DOMAIN_DEAL_VARIATION = "dealVariations";

    String ENTITY_PARENT_DEAL = "parentDeal";
    String ENTITY_DEAL_VARIATION = "dealVariations";

    @Override
    @Mapping(target = "parentDeal", source = "parentDealId")
    @Mapping(target = "dealVariations", ignore = true)
    DealEntity toEntity(Deal domain);

    @Override
    @Mapping(target = "parentDealId", source = "parentDeal.id")
    @Mapping(target = "parentDeal", ignore = true)
    @Mapping(target = "dealVariations", ignore = true)
    Deal toDomain(DealEntity entity);

    // prettier-ignore
    @Override
    default Set<String> toEntityAttributes(Set<String> domainAttributes) {
        Set<String> entityAttributes = new HashSet<>();
        domainAttributes.forEach(
            domainAttribute -> {
                if (domainAttribute.equals(DOMAIN_PARENT_DEAL)) {
                    entityAttributes.add(ENTITY_PARENT_DEAL);
                }
                else if (domainAttribute.equals(DOMAIN_DEAL_VARIATION)) {
                    entityAttributes.add(ENTITY_DEAL_VARIATION);
                }
            });
        return entityAttributes;
    }

    default DealEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        DealEntity dealEntity = new DealEntity();
        dealEntity.setId(id);
        return dealEntity;
    }
}
