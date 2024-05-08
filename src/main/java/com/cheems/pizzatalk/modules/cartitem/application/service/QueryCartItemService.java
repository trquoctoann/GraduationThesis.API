package com.cheems.pizzatalk.modules.cartitem.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.modules.cartitem.application.port.in.query.CartItemCriteria;
import com.cheems.pizzatalk.modules.cartitem.application.port.in.share.QueryCartItemUseCase;
import com.cheems.pizzatalk.modules.cartitem.application.port.out.QueryCartItemPort;
import com.cheems.pizzatalk.modules.cartitem.domain.CartItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class QueryCartItemService implements QueryCartItemUseCase {

    private final QueryCartItemPort queryCartItemPort;

    public QueryCartItemService(QueryCartItemPort queryCartItemPort) {
        this.queryCartItemPort = queryCartItemPort;
    }

    @Override
    public Optional<CartItem> findById(Long id, String... fetchAttributes) {
        CartItemCriteria criteria = new CartItemCriteria();

        RangeFilter<Long> idFilter = new RangeFilter<Long>();
        idFilter.setEquals(id);
        criteria.setId(idFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }

        return findByCriteria(criteria);
    }

    @Override
    public CartItem getById(Long id, String... fetchAttributes) {
        return findById(id, fetchAttributes).orElseThrow(() -> new BusinessException("Not found cart item with id: " + id));
    }

    @Override
    public Optional<CartItem> findByCriteria(CartItemCriteria criteria) {
        return queryCartItemPort.findByCriteria(criteria);
    }

    @Override
    public CartItem getByCriteria(CartItemCriteria criteria) {
        return findByCriteria(criteria).orElseThrow(() -> new BusinessException("Not found cart item with criteria" + criteria.toString()));
    }

    @Override
    public List<CartItem> findListByCriteria(CartItemCriteria criteria) {
        return queryCartItemPort.findListByCriteria(criteria);
    }

    @Override
    public Page<CartItem> findPageByCriteria(CartItemCriteria criteria, Pageable pageable) {
        return queryCartItemPort.findPageByCriteria(criteria, pageable);
    }

    @Override
    public List<CartItem> findListByCartId(Long cartId) {
        CartItemCriteria criteria = new CartItemCriteria();

        RangeFilter<Long> cartIdFilter = new RangeFilter<Long>();
        cartIdFilter.setEquals(cartId);
        criteria.setCartId(cartIdFilter);

        return findListByCriteria(criteria);
    }

    @Override
    public Optional<CartItem> findDuplicateCartItem(Long cartId, Long productId, Set<Long> optionDetailIds) {
        CartItemCriteria criteria = new CartItemCriteria();

        RangeFilter<Long> cartIdFilter = new RangeFilter<Long>();
        cartIdFilter.setEquals(cartId);

        RangeFilter<Long> productIdFilter = new RangeFilter<Long>();
        productIdFilter.setEquals(productId);

        RangeFilter<Long> optionDetailIdsFilter = new RangeFilter<Long>();
        optionDetailIdsFilter.setIn(new ArrayList<>(optionDetailIds));
        
        criteria.setCartId(cartIdFilter);
        criteria.setProductId(productIdFilter);
        criteria.setOptionDetailId(optionDetailIdsFilter);
        return findByCriteria(criteria);
    }
}
