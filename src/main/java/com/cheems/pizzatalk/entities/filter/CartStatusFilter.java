package com.cheems.pizzatalk.entities.filter;

import com.cheems.pizzatalk.common.filter.Filter;
import com.cheems.pizzatalk.entities.enumeration.CartStatus;

public class CartStatusFilter extends Filter<CartStatus> {

    public CartStatusFilter() {}

    public CartStatusFilter(CartStatusFilter filter) {
        super(filter);
    }

    @Override
    public CartStatusFilter copy() {
        return new CartStatusFilter(this);
    }
}
