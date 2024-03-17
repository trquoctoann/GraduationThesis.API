package com.cheems.pizzatalk.entities.filter;

import com.cheems.pizzatalk.common.filter.Filter;
import com.cheems.pizzatalk.entities.enumeration.RefillStatus;

public class RefillStatusFilter extends Filter<RefillStatus> {

    public RefillStatusFilter() {}

    public RefillStatusFilter(RefillStatusFilter filter) {
        super(filter);
    }

    @Override
    public RefillStatusFilter copy() {
        return new RefillStatusFilter(this);
    }
}
