package com.cheems.pizzatalk.entities.mapper;

import com.cheems.pizzatalk.common.mapper.EntityMapper;
import com.cheems.pizzatalk.entities.ParticipantEntity;
import com.cheems.pizzatalk.modules.participant.domain.Participant;
import java.util.HashSet;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = { UserMapper.class, ConversationMapper.class }, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ParticipantMapper extends EntityMapper<Participant, ParticipantEntity> {
    String DOMAIN_USER = "user";
    String DOMAIN_CONVERSATION = "conversation";
    String DOMAIN_CHAT_MESSAGES = "chatMessages";

    String ENTITY_USER = "user";
    String ENTITY_CONVERSATION = "conversation";
    String ENTITY_CHAT_MESSAGES = "chatMessages";

    @Override
    @Mapping(target = "user", source = "userId")
    @Mapping(target = "conversation", source = "conversationId")
    @Mapping(target = "chatMessages", ignore = true)
    ParticipantEntity toEntity(Participant domain);

    @Override
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "conversationId", source = "conversation.id")
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "conversation", ignore = true)
    @Mapping(target = "chatMessages", ignore = true)
    Participant toDomain(ParticipantEntity entity);

    // prettier-ignore
    default Set<String> toEntityAttributes(Set<String> domainAttributes) {
        Set<String> entityAttributes = new HashSet<>();
        domainAttributes.forEach(
            domainAttribute -> {
                if (domainAttribute.equals(DOMAIN_USER)) {
                    entityAttributes.add(ENTITY_USER);
                }
                if (domainAttribute.equals(DOMAIN_CONVERSATION)) {
                    entityAttributes.add(ENTITY_CONVERSATION);
                }
                if (domainAttribute.equals(DOMAIN_CHAT_MESSAGES)) {
                    entityAttributes.add(ENTITY_CHAT_MESSAGES);
                }
            }
        );
        return entityAttributes;
    }

    default ParticipantEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        ParticipantEntity participantEntity = new ParticipantEntity();
        participantEntity.setId(id);
        return participantEntity;
    }
}
