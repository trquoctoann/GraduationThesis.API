package com.cheems.pizzatalk.modules.conversation.application.port.in.share;

import com.cheems.pizzatalk.modules.conversation.application.port.in.command.CreateConversationCommand;
import com.cheems.pizzatalk.modules.conversation.application.port.in.command.UpdateConversationMetadataCommand;
import com.cheems.pizzatalk.modules.conversation.domain.Conversation;

public interface ConversationLifecycleUseCase {
    Conversation create(CreateConversationCommand command);

    Conversation updateMetadata(UpdateConversationMetadataCommand command);

    void deleteById(Long id);

    void inviteUserToConversation(Long userId, Long conversationId);

    Conversation addUserToConversation(Long userId, Long conversationId);

    Conversation removeUserOfConversation(Long userId, Long conversationId);
}
