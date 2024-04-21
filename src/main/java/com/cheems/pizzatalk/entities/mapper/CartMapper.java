package com.cheems.pizzatalk.entities.mapper;

import com.cheems.pizzatalk.common.mapper.EntityMapper;
import com.cheems.pizzatalk.entities.CartEntity;
import com.cheems.pizzatalk.modules.cart.domain.Cart;
import java.util.HashSet;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface CartMapper extends EntityMapper<Cart, CartEntity> {
    String DOMAIN_CART_ITEMS = "cartItems";
    String DOMAIN_USER = "user";

    String ENTITY_CART_ITEMS = "cartItems";
    String ENTITY_USER = "user";

    @Override
    @Mapping(target = "user", source = "userId")
    @Mapping(target = "cartItems", ignore = true)
    CartEntity toEntity(Cart domain);

    @Override
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "cartItems", ignore = true)
    @Mapping(target = "user", ignore = true)
    Cart toDomain(CartEntity entity);

    // prettier-ignore
    @Override
    default Set<String> toEntityAttributes(Set<String> domainAttributes) {
        Set<String> entityAttributes = new HashSet<>();
        domainAttributes.forEach(
            domainAttribute -> {
                if (domainAttribute.equals(DOMAIN_CART_ITEMS)) {
                    entityAttributes.add(ENTITY_CART_ITEMS);
                }
                if (domainAttribute.equals(DOMAIN_USER)) {
                    entityAttributes.add(ENTITY_USER);
                }
            });
        return entityAttributes;
    }

    default CartEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        CartEntity cartEntity = new CartEntity();
        cartEntity.setId(id);
        return cartEntity;
    }
}
