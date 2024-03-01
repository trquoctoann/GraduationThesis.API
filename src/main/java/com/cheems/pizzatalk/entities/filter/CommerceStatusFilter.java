package com.cheems.pizzatalk.entities.filter;

import com.cheems.pizzatalk.common.filter.Filter;
import com.cheems.pizzatalk.entities.enumeration.CommerceStatus;

public class CommerceStatusFilter extends Filter<CommerceStatus> {

    public CommerceStatusFilter() {}

    public CommerceStatusFilter(CommerceStatusFilter filter) {
        super(filter);
    }

    @Override
    public CommerceStatusFilter copy() {
        return new CommerceStatusFilter(this);
    }
}
