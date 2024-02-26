package com.cheems.pizzatalk.entities.filter;

import com.cheems.pizzatalk.common.filter.Filter;
import com.cheems.pizzatalk.entities.enumeration.OperationalStatus;

public class OperationalStatusFilter extends Filter<OperationalStatus> {

    public OperationalStatusFilter() {}

    public OperationalStatusFilter(OperationalStatusFilter filter) {
        super(filter);
    }

    @Override
    public OperationalStatusFilter copy() {
        return new OperationalStatusFilter(this);
    }
}
