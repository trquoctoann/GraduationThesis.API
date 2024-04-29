package com.cheems.pizzatalk.modules.cartitem.application.port.in.share;

import com.cheems.pizzatalk.modules.cartitem.application.port.in.query.CartItemCriteria;
import com.cheems.pizzatalk.modules.cartitem.domain.CartItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryCartItemUseCase {
    Optional<CartItem> findById(Long id, String... fetchAttributes);

    CartItem getById(Long id, String... fetchAttributes);

    Optional<CartItem> findByCriteria(CartItemCriteria criteria);

    CartItem getByCriteria(CartItemCriteria criteria);

    List<CartItem> findListByCriteria(CartItemCriteria criteria);

    Page<CartItem> findPageByCriteria(CartItemCriteria criteria, Pageable pageable);

    List<CartItem> findListByCartId(Long cartId);
}
