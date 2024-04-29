package com.cheems.pizzatalk.entities.mapper;

import com.cheems.pizzatalk.common.mapper.EntityMapper;
import com.cheems.pizzatalk.entities.CartItemEntity;
import com.cheems.pizzatalk.modules.cartitem.domain.CartItem;
import java.util.HashSet;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = { CartMapper.class, ProductMapper.class }, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartItemMapper extends EntityMapper<CartItem, CartItemEntity> {
    String DOMAIN_CART = "cart";
    String DOMAIN_PRODUCT = "product";
    String DOMAIN_OPTION_DETAILS = "optionDetails";

    String ENTITY_CART = "cart";
    String ENTITY_PRODUCT = "product";
    String ENTITY_OPTION_DETAILS = "cartItemOptions.optionDetail";

    @Override
    @Mapping(target = "cart", source = "cartId")
    @Mapping(target = "product", source = "productId")
    CartItemEntity toEntity(CartItem domain);

    @Override
    @Mapping(target = "cartId", source = "cart.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "product", ignore = true)
    CartItem toDomain(CartItemEntity entity);

    // prettier-ignore
    @Override
    default Set<String> toEntityAttributes(Set<String> domainAttributes) {
        Set<String> entityAttributes = new HashSet<>();
        domainAttributes.forEach(
            domainAttribute -> {
                if (domainAttribute.equals(DOMAIN_CART)) {
                    entityAttributes.add(ENTITY_CART);
                }
                if (domainAttribute.equals(DOMAIN_PRODUCT)) {
                    entityAttributes.add(ENTITY_PRODUCT);
                }
                if (domainAttribute.equals(DOMAIN_OPTION_DETAILS)) {
                    entityAttributes.add(ENTITY_OPTION_DETAILS);
                } 
            });
        return entityAttributes;
    }

    default CartItemEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        CartItemEntity cartItemEntity = new CartItemEntity();
        cartItemEntity.setId(id);
        return cartItemEntity;
    }
}
