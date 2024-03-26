package com.cheems.pizzatalk.modules.conversation.application.port.out;

import com.cheems.pizzatalk.modules.conversation.domain.Conversation;

public interface ConversationPort {
    Conversation save(Conversation conversation);

    void deleteById(Long id);
}
