package com.cheems.pizzatalk.entities.filter;

import com.cheems.pizzatalk.common.filter.Filter;
import com.cheems.pizzatalk.entities.enumeration.ParticipantStatus;

public class ParticipantStatusFilter extends Filter<ParticipantStatus> {

    public ParticipantStatusFilter() {}

    public ParticipantStatusFilter(ParticipantStatusFilter filter) {
        super(filter);
    }

    @Override
    public ParticipantStatusFilter copy() {
        return new ParticipantStatusFilter(this);
    }
}
