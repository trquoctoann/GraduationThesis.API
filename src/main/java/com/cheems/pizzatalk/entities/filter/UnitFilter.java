package com.cheems.pizzatalk.entities.filter;

import com.cheems.pizzatalk.common.filter.Filter;
import com.cheems.pizzatalk.entities.enumeration.Unit;

public class UnitFilter extends Filter<Unit> {

    public UnitFilter() {}

    public UnitFilter(UnitFilter filter) {
        super(filter);
    }

    @Override
    public UnitFilter copy() {
        return new UnitFilter(this);
    }
}
