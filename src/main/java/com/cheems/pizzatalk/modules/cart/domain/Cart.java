package com.cheems.pizzatalk.modules.cart.domain;

import com.cheems.pizzatalk.entities.enumeration.CartStatus;
import com.cheems.pizzatalk.modules.cartitem.domain.CartItem;
import com.cheems.pizzatalk.modules.user.domain.User;
import java.io.Serializable;
import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Cart implements Serializable {

    private Long id;

    @NotNull
    private CartStatus status;

    @NotNull
    private Long userId;

    private User user;

    private Set<CartItem> cartItems;
}
