package com.cheems.pizzatalk.modules.conversation.application.port.out;

import com.cheems.pizzatalk.modules.conversation.application.port.in.query.ConversationCriteria;
import com.cheems.pizzatalk.modules.conversation.domain.Conversation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryConversationPort {
    Optional<Conversation> findByCriteria(ConversationCriteria criteria);

    List<Conversation> findListByCriteria(ConversationCriteria criteria);

    Page<Conversation> findPageByCriteria(ConversationCriteria criteria, Pageable pageable);
}
