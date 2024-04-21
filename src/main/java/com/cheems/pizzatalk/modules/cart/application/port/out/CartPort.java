package com.cheems.pizzatalk.modules.cart.application.port.out;

import com.cheems.pizzatalk.modules.cart.domain.Cart;

public interface CartPort {
    Cart save(Cart cart);

    void deleteById(Long id);
}
