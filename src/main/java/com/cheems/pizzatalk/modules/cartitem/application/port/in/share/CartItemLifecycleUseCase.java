package com.cheems.pizzatalk.modules.cartitem.application.port.in.share;

import com.cheems.pizzatalk.modules.cartitem.application.port.in.command.CreateCartItemCommand;
import com.cheems.pizzatalk.modules.cartitem.application.port.in.command.UpdateCartItemCommand;
import com.cheems.pizzatalk.modules.cartitem.domain.CartItem;

public interface CartItemLifecycleUseCase {
    CartItem create(CreateCartItemCommand command);

    CartItem update(UpdateCartItemCommand command);

    void deleteById(Long id);
}
