package com.cheems.pizzatalk.entities.filter;

import com.cheems.pizzatalk.common.filter.Filter;
import com.cheems.pizzatalk.entities.enumeration.DealStatus;

public class DealStatusFilter extends Filter<DealStatus> {

    public DealStatusFilter() {}

    public DealStatusFilter(DealStatusFilter filter) {
        super(filter);
    }

    @Override
    public DealStatusFilter copy() {
        return new DealStatusFilter(this);
    }
}
