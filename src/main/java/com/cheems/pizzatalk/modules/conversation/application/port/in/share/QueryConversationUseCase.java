package com.cheems.pizzatalk.modules.conversation.application.port.in.share;

import com.cheems.pizzatalk.modules.conversation.application.port.in.query.ConversationCriteria;
import com.cheems.pizzatalk.modules.conversation.domain.Conversation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryConversationUseCase {
    Optional<Conversation> findById(Long id, String... fetchAttributes);

    Conversation getById(Long id, String... fetchAttributes);

    Optional<Conversation> findByCriteria(ConversationCriteria criteria);

    Conversation getByCriteria(ConversationCriteria criteria);

    List<Conversation> findListByCriteria(ConversationCriteria criteria);

    Page<Conversation> findPageByCriteria(ConversationCriteria criteria, Pageable pageable);

    List<Conversation> findListPrivateByUsernames(List<String> usernames);

    List<Conversation> findListByUserId(Long userId);
}
