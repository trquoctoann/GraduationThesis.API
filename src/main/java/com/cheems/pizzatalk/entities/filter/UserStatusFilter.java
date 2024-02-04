package com.cheems.pizzatalk.entities.filter;

import com.cheems.pizzatalk.common.filter.Filter;
import com.cheems.pizzatalk.entities.enumeration.UserStatus;

public class UserStatusFilter extends Filter<UserStatus> {

    public UserStatusFilter() {}

    public UserStatusFilter(UserStatusFilter filter) {
        super(filter);
    }

    @Override
    public UserStatusFilter copy() {
        return new UserStatusFilter(this);
    }
}
