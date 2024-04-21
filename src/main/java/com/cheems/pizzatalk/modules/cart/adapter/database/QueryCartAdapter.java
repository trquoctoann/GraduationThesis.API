package com.cheems.pizzatalk.modules.cart.adapter.database;

import com.cheems.pizzatalk.common.exception.AdapterException;
import com.cheems.pizzatalk.common.service.QueryService;
import com.cheems.pizzatalk.common.specification.SpecificationUtils;
import com.cheems.pizzatalk.entities.CartEntity;
import com.cheems.pizzatalk.entities.CartEntity_;
import com.cheems.pizzatalk.entities.CartItemEntity_;
import com.cheems.pizzatalk.entities.UserEntity_;
import com.cheems.pizzatalk.entities.mapper.CartItemMapper;
import com.cheems.pizzatalk.entities.mapper.CartMapper;
import com.cheems.pizzatalk.entities.mapper.UserMapper;
import com.cheems.pizzatalk.modules.cart.application.port.in.query.CartCriteria;
import com.cheems.pizzatalk.modules.cart.application.port.out.QueryCartPort;
import com.cheems.pizzatalk.modules.cart.domain.Cart;
import com.cheems.pizzatalk.repository.CartRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.criteria.JoinType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class QueryCartAdapter extends QueryService<CartEntity> implements QueryCartPort {

    private final CartRepository cartRepository;

    private final CartMapper cartMapper;

    private final UserMapper userMapper;

    private final CartItemMapper cartItemMapper;

    public QueryCartAdapter(CartRepository cartRepository, CartMapper cartMapper, UserMapper userMapper, CartItemMapper cartItemMapper) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.userMapper = userMapper;
        this.cartItemMapper = cartItemMapper;
    }

    @Override
    public Optional<Cart> findByCriteria(CartCriteria criteria) {
        List<CartEntity> cartEntities = cartRepository.findAll(createSpecification(criteria));
        if (CollectionUtils.isEmpty(cartEntities)) {
            return Optional.empty();
        }
        Integer cartEntitiesSize = cartEntities.size();
        if (cartEntitiesSize > 1) {
            Set<Long> cartIds = cartEntities.stream().map(CartEntity::getId).collect(Collectors.toSet());
            throw new AdapterException("Found more than one cart: " + cartIds);
        }
        return Optional.of(cartEntities.get(0)).map(cartEntity -> toDomainModel(cartEntity, criteria.getFetchAttributes()));
    }

    @Override
    public List<Cart> findListByCriteria(CartCriteria criteria) {
        return cartRepository
            .findAll(createSpecification(criteria))
            .stream()
            .map(cartEntity -> toDomainModel(cartEntity, criteria.getFetchAttributes()))
            .collect(Collectors.toList());
    }

    @Override
    public Page<Cart> findPageByCriteria(CartCriteria criteria, Pageable pageable) {
        Page<CartEntity> cartEntitiesPage = cartRepository.findAll(createSpecification(criteria), pageable);
        return new PageImpl<>(cartMapper.toDomain(cartEntitiesPage.getContent()), pageable, cartEntitiesPage.getTotalElements());
    }

    private Specification<CartEntity> createSpecification(CartCriteria criteria) {
        Set<String> fetchAttributes = criteria.getFetchAttributes();
        Specification<CartEntity> specification = Specification.where(null);

        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CartEntity_.id));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), CartEntity_.status));
            }
            if (criteria.getCartItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCartItemId(),
                            root -> root.join(CartEntity_.cartItems, JoinType.LEFT).get(CartItemEntity_.id)
                        )
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(CartEntity_.user, JoinType.LEFT).get(UserEntity_.id))
                    );
            }
            if (!CollectionUtils.isEmpty(fetchAttributes)) {
                specification = specification.and(SpecificationUtils.fetchAttributes(cartMapper.toEntityAttributes(fetchAttributes)));
            }
            specification = specification.and(SpecificationUtils.distinct(true));
        }
        return specification;
    }

    private Cart toDomainModel(CartEntity cartEntity, Set<String> domainAttributes) {
        Cart cart = cartMapper.toDomain(cartEntity);
        return enrichCartDomain(cart, cartEntity, domainAttributes);
    }

    private Cart enrichCartDomain(Cart cart, CartEntity cartEntity, Set<String> domainAttributes) {
        if (CollectionUtils.isEmpty(domainAttributes)) {
            return cart;
        }

        if (domainAttributes.contains(CartMapper.DOMAIN_CART_ITEMS)) {
            cart.setCartItems(
                cartEntity.getCartItems().stream().map(cartItem -> cartItemMapper.toDomain(cartItem)).collect(Collectors.toSet())
            );
        }
        if (domainAttributes.contains(CartMapper.DOMAIN_USER)) {
            cart.setUser(userMapper.toDomain(cartEntity.getUser()));
        }
        return cart;
    }
}
