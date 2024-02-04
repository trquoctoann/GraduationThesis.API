package com.cheems.pizzatalk.entities.filter;

import com.cheems.pizzatalk.common.filter.Filter;
import com.cheems.pizzatalk.entities.enumeration.KeyStatus;

public class KeyStatusFilter extends Filter<KeyStatus> {

    public KeyStatusFilter() {}

    public KeyStatusFilter(KeyStatusFilter filter) {
        super(filter);
    }

    @Override
    public KeyStatusFilter copy() {
        return new KeyStatusFilter(this);
    }
}
