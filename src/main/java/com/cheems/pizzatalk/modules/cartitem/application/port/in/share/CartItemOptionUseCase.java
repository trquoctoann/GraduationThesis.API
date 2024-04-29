package com.cheems.pizzatalk.modules.cartitem.application.port.in.share;

import java.util.Set;

public interface CartItemOptionUseCase {
    void saveOptionDetailToCartItem(Long cartItemId, Set<Long> optionDetailIds);

    void removeAllOptionOfCartItem(Long cartItemId);
}
