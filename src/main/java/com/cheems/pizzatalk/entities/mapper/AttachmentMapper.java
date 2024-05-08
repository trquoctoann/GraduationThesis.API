package com.cheems.pizzatalk.entities.mapper;

import com.cheems.pizzatalk.common.mapper.EntityMapper;
import com.cheems.pizzatalk.entities.AttachmentEntity;
import com.cheems.pizzatalk.modules.attachment.domain.Attachment;
import java.util.HashSet;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = { ChatMessageMapper.class }, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AttachmentMapper extends EntityMapper<Attachment, AttachmentEntity> {
    String DOMAIN_CHAT_MESSAGE = "chatMessage";

    String ENTITY_CHAT_MESSAGE = "chatMessage";

    @Override
    @Mapping(target = "chatMessage", source = "chatMessageId")
    AttachmentEntity toEntity(Attachment domain);

    @Override
    @Mapping(target = "chatMessageId", source = "chatMessage.id")
    @Mapping(target = "chatMessage", ignore = true)
    Attachment toDomain(AttachmentEntity entity);

    // prettier-ignore
    @Override
    default Set<String> toEntityAttributes(Set<String> domainAttributes) {
        Set<String> entityAttributes = new HashSet<>();
        domainAttributes.forEach(
            domainAttribute -> {
                if (domainAttribute.equals(DOMAIN_CHAT_MESSAGE)) {
                    entityAttributes.add(ENTITY_CHAT_MESSAGE);
                }
            });
        return entityAttributes;
    }

    default AttachmentEntity fromId(Long id) {
        if (id == null) {
            return null;
        }
        AttachmentEntity attachmentEntity = new AttachmentEntity();
        attachmentEntity.setId(id);
        return attachmentEntity;
    }
}
