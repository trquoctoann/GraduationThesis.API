package com.cheems.pizzatalk.modules.cartitem.domain;

import com.cheems.pizzatalk.modules.cart.domain.Cart;
import com.cheems.pizzatalk.modules.optiondetail.domain.OptionDetail;
import com.cheems.pizzatalk.modules.product.domain.Product;
import java.io.Serializable;
import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CartItem implements Serializable {

    private Long id;

    @NotNull
    private Integer quantity;

    @NotNull
    private Float price;

    private Long cartId;

    private Cart cart;

    private Long productId;

    private Product product;

    private Set<OptionDetail> optionDetails;
}
