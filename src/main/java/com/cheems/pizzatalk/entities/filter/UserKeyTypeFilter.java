package com.cheems.pizzatalk.entities.filter;

import com.cheems.pizzatalk.common.filter.Filter;
import com.cheems.pizzatalk.entities.enumeration.UserKeyType;

public class UserKeyTypeFilter extends Filter<UserKeyType> {

    public UserKeyTypeFilter() {}

    public UserKeyTypeFilter(UserKeyTypeFilter filter) {
        super(filter);
    }

    @Override
    public UserKeyTypeFilter copy() {
        return new UserKeyTypeFilter(this);
    }
}
