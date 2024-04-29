package com.cheems.pizzatalk.modules.cartitem.application.port.out;

import com.cheems.pizzatalk.modules.cartitem.application.port.in.query.CartItemCriteria;
import com.cheems.pizzatalk.modules.cartitem.domain.CartItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryCartItemPort {
    Optional<CartItem> findByCriteria(CartItemCriteria criteria);

    List<CartItem> findListByCriteria(CartItemCriteria criteria);

    Page<CartItem> findPageByCriteria(CartItemCriteria criteria, Pageable pageable);
}
