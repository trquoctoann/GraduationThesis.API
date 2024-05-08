package com.cheems.pizzatalk.entities.filter;

import com.cheems.pizzatalk.common.filter.Filter;
import com.cheems.pizzatalk.entities.enumeration.ChatMessageType;

public class ChatMessageTypeFilter extends Filter<ChatMessageType> {

    public ChatMessageTypeFilter() {}

    public ChatMessageTypeFilter(ChatMessageTypeFilter filter) {
        super(filter);
    }

    @Override
    public ChatMessageTypeFilter copy() {
        return new ChatMessageTypeFilter(this);
    }
}
