package com.cheems.pizzatalk.entities.mapper;

import com.cheems.pizzatalk.common.mapper.EntityMapper;
import com.cheems.pizzatalk.entities.ConversationEntity;
import com.cheems.pizzatalk.modules.conversation.domain.Conversation;
import java.util.HashSet;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ConversationMapper extends EntityMapper<Conversation, ConversationEntity> {
    String DOMAIN_USER = "users";
    String DOMAIN_CHAT_MESSAGES = "chatMessages";

    String ENTITY_USER = "participants.user";
    String ENTITY_CHAT_MESSAGES = "chatMessages";

    @Override
    @Mapping(target = "chatMessages", ignore = true)
    ConversationEntity toEntity(Conversation domain);

    @Override
    @Mapping(target = "chatMessages", ignore = true)
    Conversation toDomain(ConversationEntity entity);

    // prettier-ignore
    default Set<String> toEntityAttributes(Set<String> domainAttributes) {
        Set<String> entityAttributes = new HashSet<>();
        domainAttributes.forEach(
            domainAttribute -> {
                if (domainAttribute.equals(DOMAIN_USER)) {
                    entityAttributes.add(ENTITY_USER);
                }
                if (domainAttribute.equals(DOMAIN_CHAT_MESSAGES)) {
                    entityAttributes.add(ENTITY_CHAT_MESSAGES);
                }
            }
        );
        return entityAttributes;
    }

    default ConversationEntity fromId(Long id) {
        if (id != null) {
            return null;
        }
        ConversationEntity conversationEntity = new ConversationEntity();
        conversationEntity.setId(id);
        return conversationEntity;
    }
}
