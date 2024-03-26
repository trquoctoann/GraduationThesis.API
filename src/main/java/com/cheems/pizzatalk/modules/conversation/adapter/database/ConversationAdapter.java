package com.cheems.pizzatalk.modules.conversation.adapter.database;

import com.cheems.pizzatalk.entities.mapper.ConversationMapper;
import com.cheems.pizzatalk.modules.conversation.application.port.out.ConversationPort;
import com.cheems.pizzatalk.modules.conversation.domain.Conversation;
import com.cheems.pizzatalk.repository.ConversationRepository;
import org.springframework.stereotype.Component;

@Component
public class ConversationAdapter implements ConversationPort {

    private final ConversationRepository conversationRepository;

    private final ConversationMapper conversationMapper;

    public ConversationAdapter(ConversationRepository conversationRepository, ConversationMapper conversationMapper) {
        this.conversationRepository = conversationRepository;
        this.conversationMapper = conversationMapper;
    }

    @Override
    public Conversation save(Conversation conversation) {
        return conversationMapper.toDomain(conversationRepository.save(conversationMapper.toEntity(conversation)));
    }

    @Override
    public void deleteById(Long id) {
        conversationRepository.deleteById(id);
    }
}
