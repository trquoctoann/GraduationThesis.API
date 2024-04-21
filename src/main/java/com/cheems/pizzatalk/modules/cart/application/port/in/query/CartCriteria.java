package com.cheems.pizzatalk.modules.cart.application.port.in.query;

import com.cheems.pizzatalk.common.cqrs.QuerySelfValidating;
import com.cheems.pizzatalk.common.criteria.Criteria;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.entities.filter.CartStatusFilter;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class CartCriteria extends QuerySelfValidating<CartCriteria> implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private RangeFilter<Long> id;

    private CartStatusFilter status;

    private RangeFilter<Long> cartItemId;

    private RangeFilter<Long> userId;

    public CartCriteria() {}

    public CartCriteria(CartCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.cartItemId = other.cartItemId == null ? null : other.cartItemId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public CartCriteria copy() {
        return new CartCriteria(this);
    }
}
