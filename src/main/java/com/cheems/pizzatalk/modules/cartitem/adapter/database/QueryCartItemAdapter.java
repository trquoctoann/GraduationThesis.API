package com.cheems.pizzatalk.modules.cartitem.adapter.database;

import com.cheems.pizzatalk.common.exception.AdapterException;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.service.QueryService;
import com.cheems.pizzatalk.common.specification.SpecificationUtils;
import com.cheems.pizzatalk.entities.CartEntity_;
import com.cheems.pizzatalk.entities.CartItemEntity;
import com.cheems.pizzatalk.entities.CartItemEntity_;
import com.cheems.pizzatalk.entities.CartItemOptionEntity;
import com.cheems.pizzatalk.entities.OptionDetailEntity;
import com.cheems.pizzatalk.entities.OptionDetailEntity_;
import com.cheems.pizzatalk.entities.ProductEntity_;
import com.cheems.pizzatalk.entities.mapper.CartItemMapper;
import com.cheems.pizzatalk.entities.mapper.CartMapper;
import com.cheems.pizzatalk.entities.mapper.OptionDetailMapper;
import com.cheems.pizzatalk.entities.mapper.ProductMapper;
import com.cheems.pizzatalk.modules.cartitem.application.port.in.query.CartItemCriteria;
import com.cheems.pizzatalk.modules.cartitem.application.port.out.QueryCartItemPort;
import com.cheems.pizzatalk.modules.cartitem.domain.CartItem;
import com.cheems.pizzatalk.repository.CartItemRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class QueryCartItemAdapter extends QueryService<CartItemEntity> implements QueryCartItemPort {

    private final CartItemRepository cartItemRepository;

    private final CartItemMapper cartItemMapper;

    private final CartMapper cartMapper;

    private final ProductMapper productMapper;

    private final OptionDetailMapper optionDetailMapper;

    public QueryCartItemAdapter(
        CartItemRepository cartItemRepository,
        CartItemMapper cartItemMapper,
        CartMapper cartMapper,
        ProductMapper productMapper,
        OptionDetailMapper optionDetailMapper
    ) {
        this.cartItemRepository = cartItemRepository;
        this.cartItemMapper = cartItemMapper;
        this.cartMapper = cartMapper;
        this.productMapper = productMapper;
        this.optionDetailMapper = optionDetailMapper;
    }

    @Override
    public Optional<CartItem> findByCriteria(CartItemCriteria criteria) {
        List<CartItemEntity> cartItemEntities = cartItemRepository.findAll(createSpecification(criteria));
        if (CollectionUtils.isEmpty(cartItemEntities)) {
            return Optional.empty();
        }

        Integer cartItemEntitiesSize = cartItemEntities.size();
        if (cartItemEntitiesSize > 1) {
            Set<Long> cartItemIds = cartItemEntities.stream().map(CartItemEntity::getId).collect(Collectors.toSet());
            throw new AdapterException("Found more than one cart item: " + cartItemIds);
        }
        return Optional.of(cartItemEntities.get(0)).map(cartItemEntity -> toDomainModel(cartItemEntity, criteria.getFetchAttributes()));
    }

    @Override
    public List<CartItem> findListByCriteria(CartItemCriteria criteria) {
        return cartItemRepository
            .findAll(createSpecification(criteria))
            .stream()
            .map(cartItemEntity -> toDomainModel(cartItemEntity, criteria.getFetchAttributes()))
            .collect(Collectors.toList());
    }

    @Override
    public Page<CartItem> findPageByCriteria(CartItemCriteria criteria, Pageable pageable) {
        Page<CartItemEntity> cartEntitiesPage = cartItemRepository.findAll(createSpecification(criteria), pageable);
        return new PageImpl<>(cartItemMapper.toDomain(cartEntitiesPage.getContent()), pageable, cartEntitiesPage.getTotalElements());
    }

    private Specification<CartItemEntity> createSpecification(CartItemCriteria criteria) {
        Set<String> fetchAttributes = criteria.getFetchAttributes();
        Specification<CartItemEntity> specification = Specification.where(null);

        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CartItemEntity_.id));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), CartItemEntity_.quantity));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), CartItemEntity_.price));
            }
            if (criteria.getCartId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCartId(), root -> root.join(CartItemEntity_.cart, JoinType.LEFT).get(CartEntity_.id))
                    );
            }
            if (criteria.getProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductId(),
                            root -> root.join(CartItemEntity_.product, JoinType.LEFT).get(ProductEntity_.id)
                        )
                    );
            }
            if (criteria.getOptionDetailId() != null) {
                specification = specification.and(buildSpecificationFindByOptionDetailId(criteria.getOptionDetailId()));
            }
            if (!CollectionUtils.isEmpty(fetchAttributes)) {
                specification = specification.and(SpecificationUtils.fetchAttributes(cartItemMapper.toEntityAttributes(fetchAttributes)));
            }
            specification = specification.and(SpecificationUtils.distinct(true));
        }
        return specification;
    }

    private Specification<CartItemEntity> buildSpecificationFindByOptionDetailId(RangeFilter<Long> optionDetailId) {
        if (optionDetailId.getIn() != null) {
            return (root, query, builder) -> {
                Join<CartItemEntity, CartItemOptionEntity> joinCartItemOption = SpecificationUtils.getJoinFetch(root, "cartItemOptions", JoinType.LEFT, false);
                Join<CartItemOptionEntity, OptionDetailEntity> joinOptionDetail = SpecificationUtils.getJoinFetch(joinCartItemOption, "optionDetail", JoinType.LEFT, false);
                return joinOptionDetail.get(OptionDetailEntity_.id).in(optionDetailId.getIn());
            };
        }
        return null;
    }

    private CartItem toDomainModel(CartItemEntity cartItemEntity, Set<String> domainAttributes) {
        CartItem cartItem = cartItemMapper.toDomain(cartItemEntity);
        return enrichCartItemDomain(cartItem, cartItemEntity, domainAttributes);
    }

    private CartItem enrichCartItemDomain(CartItem cartItem, CartItemEntity cartItemEntity, Set<String> domainAttributes) {
        if (CollectionUtils.isEmpty(domainAttributes)) {
            return cartItem;
        }

        if (domainAttributes.contains(CartItemMapper.DOMAIN_CART)) {
            cartItem.setCart(cartMapper.toDomain(cartItemEntity.getCart()));
        }
        if (domainAttributes.contains(CartItemMapper.DOMAIN_PRODUCT)) {
            cartItem.setProduct(productMapper.toDomain(cartItemEntity.getProduct()));
        }
        if (domainAttributes.contains(CartItemMapper.DOMAIN_OPTION_DETAILS)) {
            cartItem.setOptionDetails(
                cartItemEntity
                    .getCartItemOptions()
                    .stream()
                    .map(cartItemOptionEntity -> optionDetailMapper.toDomain(cartItemOptionEntity.getOptionDetail()))
                    .collect(Collectors.toSet())
            );
        }
        return cartItem;
    }
}
