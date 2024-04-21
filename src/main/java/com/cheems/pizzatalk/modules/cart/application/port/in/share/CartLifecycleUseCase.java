package com.cheems.pizzatalk.modules.cart.application.port.in.share;

import com.cheems.pizzatalk.modules.cart.application.port.in.command.CreateCartCommand;
import com.cheems.pizzatalk.modules.cart.application.port.in.command.UpdateCartCommand;
import com.cheems.pizzatalk.modules.cart.domain.Cart;

public interface CartLifecycleUseCase {
    Cart create(CreateCartCommand command);

    Cart update(UpdateCartCommand command);

    void deleteById(Long id);

    Cart checkoutCart(Long id);
}
