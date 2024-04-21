package com.cheems.pizzatalk.modules.cart.application.port.in.share;

import com.cheems.pizzatalk.entities.enumeration.CartStatus;
import com.cheems.pizzatalk.modules.cart.application.port.in.query.CartCriteria;
import com.cheems.pizzatalk.modules.cart.domain.Cart;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryCartUseCase {
    Optional<Cart> findById(Long id, String... fetchAttributes);

    Cart getById(Long id, String... fetchAttributes);

    Optional<Cart> findByCriteria(CartCriteria criteria);

    Cart getByCriteria(CartCriteria criteria);

    List<Cart> findListByCriteria(CartCriteria criteria);

    Page<Cart> findPageByCriteria(CartCriteria criteria, Pageable pageable);

    List<Cart> findListByStatus(CartStatus status);
}
