package com.cheems.pizzatalk.modules.cart.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.entities.enumeration.CartStatus;
import com.cheems.pizzatalk.entities.filter.CartStatusFilter;
import com.cheems.pizzatalk.modules.cart.application.port.in.query.CartCriteria;
import com.cheems.pizzatalk.modules.cart.application.port.in.share.QueryCartUseCase;
import com.cheems.pizzatalk.modules.cart.application.port.out.QueryCartPort;
import com.cheems.pizzatalk.modules.cart.domain.Cart;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class QueryCartService implements QueryCartUseCase {

    private final QueryCartPort queryCartPort;

    public QueryCartService(QueryCartPort queryCartPort) {
        this.queryCartPort = queryCartPort;
    }

    @Override
    public Optional<Cart> findById(Long id, String... fetchAttributes) {
        CartCriteria criteria = new CartCriteria();

        RangeFilter<Long> idFilter = new RangeFilter<Long>();
        idFilter.setEquals(id);
        criteria.setId(idFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }
        return findByCriteria(criteria);
    }

    @Override
    public Cart getById(Long id, String... fetchAttributes) {
        return findById(id, fetchAttributes).orElseThrow(() -> new BusinessException("Not found cart with id: " + id));
    }

    @Override
    public Optional<Cart> findByCriteria(CartCriteria criteria) {
        return queryCartPort.findByCriteria(criteria);
    }

    @Override
    public Cart getByCriteria(CartCriteria criteria) {
        return findByCriteria(criteria).orElseThrow(() -> new BusinessException("Not found cart with criteria: " + criteria));
    }

    @Override
    public List<Cart> findListByCriteria(CartCriteria criteria) {
        return queryCartPort.findListByCriteria(criteria);
    }

    @Override
    public Page<Cart> findPageByCriteria(CartCriteria criteria, Pageable pageable) {
        return queryCartPort.findPageByCriteria(criteria, pageable);
    }

    @Override
    public List<Cart> findListByStatus(CartStatus status) {
        CartCriteria criteria = new CartCriteria();

        CartStatusFilter statusFilter = new CartStatusFilter();
        statusFilter.setEquals(status);
        criteria.setStatus(statusFilter);

        return findListByCriteria(criteria);
    }
}
