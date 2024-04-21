package com.cheems.pizzatalk.modules.cart.application.port.out;

import com.cheems.pizzatalk.modules.cart.application.port.in.query.CartCriteria;
import com.cheems.pizzatalk.modules.cart.domain.Cart;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryCartPort {
    Optional<Cart> findByCriteria(CartCriteria criteria);

    List<Cart> findListByCriteria(CartCriteria criteria);

    Page<Cart> findPageByCriteria(CartCriteria criteria, Pageable pageable);
}
