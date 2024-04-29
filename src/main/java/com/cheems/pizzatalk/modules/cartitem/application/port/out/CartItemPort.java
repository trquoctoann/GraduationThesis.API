package com.cheems.pizzatalk.modules.cartitem.application.port.out;

import com.cheems.pizzatalk.modules.cartitem.domain.CartItem;
import java.util.Set;

public interface CartItemPort {
    CartItem save(CartItem cartItem);

    void saveOptionDetail(Long cartItemId, Set<Long> optionDetailIds);

    void removeOptionDetail(Long cartItemId, Set<Long> optionDetailIds);

    void removeAllOptionOfCartItem(Long cartItemId);

    void deleteById(Long id);
}
