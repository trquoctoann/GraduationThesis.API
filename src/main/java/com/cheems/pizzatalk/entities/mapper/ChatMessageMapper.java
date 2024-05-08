package com.cheems.pizzatalk.entities.mapper;

import com.cheems.pizzatalk.common.mapper.EntityMapper;
import com.cheems.pizzatalk.entities.ChatMessageEntity;
import com.cheems.pizzatalk.modules.chatmessage.domain.ChatMessage;
import java.util.HashSet;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    uses = { ConversationMapper.class, ParticipantMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ChatMessageMapper extends EntityMapper<ChatMessage, ChatMessageEntity> {
    String DOMAIN_CONVERSATION = "conversation";
    String DOMAIN_PARTICIPANT = "participant";
    String DOMAIN_ATTACHMENTS = "attachments";

    String ENTITY_CONVERSATION = "conversation";
    String ENTITY_PARTICIPANT = "participant";
    String ENTITY_ATTACHMENTS = "attachments";

    @Override
    @Mapping(target = "conversation", source = "conversationId")
    @Mapping(target = "participant", source = "participantId")
    @Mapping(target = "attachments", ignore = true)
    ChatMessageEntity toEntity(ChatMessage domain);

    @Override
    @Mapping(target = "conversationId", source = "conversation.id")
    @Mapping(target = "participantId", source = "participant.id")
    @Mapping(target = "conversation", ignore = true)
    @Mapping(target = "participant", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    ChatMessage toDomain(ChatMessageEntity entity);

    // prettier-ignore
    default Set<String> toEntityAttributes(Set<String> domainAttributes) {
        Set<String> entityAttributes = new HashSet<>();
        domainAttributes.forEach(
            domainAttribute -> {
                if (domainAttribute.equals(DOMAIN_CONVERSATION)) {
                    entityAttributes.add(ENTITY_CONVERSATION);
                }
                if (domainAttribute.equals(DOMAIN_PARTICIPANT)) {
                    entityAttributes.add(ENTITY_PARTICIPANT);
                }
                if (domainAttribute.equals(DOMAIN_ATTACHMENTS)) {
                    entityAttributes.add(ENTITY_ATTACHMENTS);
                }
            }
        );
        return entityAttributes;
    }

    default ChatMessageEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        ChatMessageEntity messageEntity = new ChatMessageEntity();
        messageEntity.setId(id);
        return messageEntity;
    }
}
