package com.cheems.pizzatalk.modules.cartitem.application.port.in.query;

import com.cheems.pizzatalk.common.cqrs.QuerySelfValidating;
import com.cheems.pizzatalk.common.criteria.Criteria;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class CartItemCriteria extends QuerySelfValidating<CartItemCriteria> implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private RangeFilter<Long> id;

    private RangeFilter<Integer> quantity;

    private RangeFilter<Float> price;

    private RangeFilter<Long> cartId;

    private RangeFilter<Long> productId;

    private RangeFilter<Long> optionDetailId;

    public CartItemCriteria() {}

    public CartItemCriteria(CartItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.cartId = other.cartId == null ? null : other.cartId.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.optionDetailId = other.optionDetailId == null ? null : other.optionDetailId.copy();
    }

    @Override
    public CartItemCriteria copy() {
        return new CartItemCriteria(this);
    }
}
