package com.cheems.pizzatalk.entities.filter;

import com.cheems.pizzatalk.common.filter.Filter;
import com.cheems.pizzatalk.entities.enumeration.ConversationType;

public class ConversationTypeFilter extends Filter<ConversationType> {

    public ConversationTypeFilter() {}

    public ConversationTypeFilter(ConversationTypeFilter filter) {
        super(filter);
    }

    @Override
    public ConversationTypeFilter copy() {
        return new ConversationTypeFilter(this);
    }
}
