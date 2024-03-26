package com.cheems.pizzatalk.modules.participant.adapter.database;

import com.cheems.pizzatalk.common.exception.AdapterException;
import com.cheems.pizzatalk.common.service.QueryService;
import com.cheems.pizzatalk.common.specification.SpecificationUtils;
import com.cheems.pizzatalk.entities.ConversationEntity_;
import com.cheems.pizzatalk.entities.ParticipantEntity;
import com.cheems.pizzatalk.entities.ParticipantEntity_;
import com.cheems.pizzatalk.entities.UserEntity_;
import com.cheems.pizzatalk.entities.mapper.ChatMessageMapper;
import com.cheems.pizzatalk.entities.mapper.ConversationMapper;
import com.cheems.pizzatalk.entities.mapper.ParticipantMapper;
import com.cheems.pizzatalk.entities.mapper.UserMapper;
import com.cheems.pizzatalk.modules.participant.application.port.in.query.ParticipantCriteria;
import com.cheems.pizzatalk.modules.participant.application.port.out.QueryParticipantPort;
import com.cheems.pizzatalk.modules.participant.domain.Participant;
import com.cheems.pizzatalk.repository.ParticipantRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.criteria.JoinType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class QueryParticipantAdapter extends QueryService<ParticipantEntity> implements QueryParticipantPort {

    private final ParticipantRepository participantRepository;

    private final ParticipantMapper participantMapper;

    private final UserMapper userMapper;

    private final ConversationMapper conversationMapper;

    private final ChatMessageMapper chatMessageMapper;

    public QueryParticipantAdapter(
        ParticipantRepository participantRepository,
        ParticipantMapper participantMapper,
        UserMapper userMapper,
        ConversationMapper conversationMapper,
        ChatMessageMapper chatMessageMapper
    ) {
        this.participantRepository = participantRepository;
        this.participantMapper = participantMapper;
        this.userMapper = userMapper;
        this.conversationMapper = conversationMapper;
        this.chatMessageMapper = chatMessageMapper;
    }

    @Override
    public Optional<Participant> findByCriteria(ParticipantCriteria criteria) {
        List<ParticipantEntity> participantEntities = participantRepository.findAll(createSpecification(criteria));
        if (CollectionUtils.isEmpty(participantEntities)) {
            return Optional.empty();
        }
        Integer participantEntitiesSize = participantEntities.size();
        if (participantEntitiesSize > 1) {
            Set<Long> participantIds = participantEntities.stream().map(ParticipantEntity::getId).collect(Collectors.toSet());
            throw new AdapterException("Found more than one participant: " + participantIds);
        }
        return Optional
            .of(participantEntities.get(0))
            .map(participantEntity -> toDomainModel(participantEntity, criteria.getFetchAttributes()));
    }

    @Override
    public List<Participant> findListByCriteria(ParticipantCriteria criteria) {
        return participantRepository
            .findAll(createSpecification(criteria))
            .stream()
            .map(participantEntity -> toDomainModel(participantEntity, criteria.getFetchAttributes()))
            .collect(Collectors.toList());
    }

    @Override
    public Page<Participant> findPageByCriteria(ParticipantCriteria criteria, Pageable pageable) {
        Page<ParticipantEntity> participantEntitiesPage = participantRepository.findAll(createSpecification(criteria), pageable);
        return new PageImpl<>(
            participantMapper.toDomain(participantEntitiesPage.getContent()),
            pageable,
            participantEntitiesPage.getTotalElements()
        );
    }

    private Specification<ParticipantEntity> createSpecification(ParticipantCriteria criteria) {
        Set<String> fetchAttributes = criteria.getFetchAttributes();
        Specification<ParticipantEntity> specification = Specification.where(null);

        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ParticipantEntity_.id));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), ParticipantEntity_.status));
            }
            if (criteria.getJoinedAt() != null) {
                specification = specification.and(buildSpecification(criteria.getJoinedAt(), ParticipantEntity_.joinedAt));
            }
            if (criteria.getLeftAt() != null) {
                specification = specification.and(buildSpecification(criteria.getLeftAt(), ParticipantEntity_.leftAt));
            }
            if (criteria.getDeletedAt() != null) {
                specification = specification.and(buildSpecification(criteria.getDeletedAt(), ParticipantEntity_.deletedAt));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUserId(),
                            root -> root.join(ParticipantEntity_.user, JoinType.LEFT).get(UserEntity_.id)
                        )
                    );
            }
            if (criteria.getConversationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getConversationId(),
                            root -> root.join(ParticipantEntity_.conversation, JoinType.LEFT).get(ConversationEntity_.id)
                        )
                    );
            }
            if (!CollectionUtils.isEmpty(fetchAttributes)) {
                specification =
                    specification.and(SpecificationUtils.fetchAttributes(participantMapper.toEntityAttributes(fetchAttributes)));
            }
            specification = specification.and(SpecificationUtils.distinct(true));
        }
        return specification;
    }

    private Participant toDomainModel(ParticipantEntity participantEntity, Set<String> domainAttributes) {
        Participant participant = participantMapper.toDomain(participantEntity);
        return enrichParticipantDomain(participant, participantEntity, domainAttributes);
    }

    private Participant enrichParticipantDomain(
        Participant participant,
        ParticipantEntity participantEntity,
        Set<String> domainAttributes
    ) {
        if (CollectionUtils.isEmpty(domainAttributes)) {
            return participant;
        }

        if (domainAttributes.contains(ParticipantMapper.DOMAIN_USER)) {
            participant.setUser(userMapper.toDomain(participantEntity.getUser()));
        }
        if (domainAttributes.contains(ParticipantMapper.DOMAIN_CONVERSATION)) {
            participant.setConversation(conversationMapper.toDomain(participantEntity.getConversation()));
        }
        if (domainAttributes.contains(ParticipantMapper.DOMAIN_CHAT_MESSAGES)) {
            participant.setChatMessages(
                participantEntity
                    .getChatMessages()
                    .stream()
                    .map(chatMessageEntity -> chatMessageMapper.toDomain(chatMessageEntity))
                    .collect(Collectors.toSet())
            );
        }
        return participant;
    }
}
