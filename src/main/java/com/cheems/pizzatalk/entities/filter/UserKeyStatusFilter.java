package com.cheems.pizzatalk.entities.filter;

import com.cheems.pizzatalk.common.filter.Filter;
import com.cheems.pizzatalk.entities.enumeration.UserKeyStatus;

public class UserKeyStatusFilter extends Filter<UserKeyStatus> {

    public UserKeyStatusFilter() {}

    public UserKeyStatusFilter(UserKeyStatusFilter filter) {
        super(filter);
    }

    @Override
    public UserKeyStatusFilter copy() {
        return new UserKeyStatusFilter(this);
    }
}
