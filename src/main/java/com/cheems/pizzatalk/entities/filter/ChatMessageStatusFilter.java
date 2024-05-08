package com.cheems.pizzatalk.entities.filter;

import com.cheems.pizzatalk.common.filter.Filter;
import com.cheems.pizzatalk.entities.enumeration.ChatMessageStatus;

public class ChatMessageStatusFilter extends Filter<ChatMessageStatus> {

    public ChatMessageStatusFilter() {}

    public ChatMessageStatusFilter(ChatMessageStatusFilter filter) {
        super(filter);
    }

    @Override
    public ChatMessageStatusFilter copy() {
        return new ChatMessageStatusFilter(this);
    }
}
