package com.cheems.pizzatalk.modules.conversation.adapter.database;

import com.cheems.pizzatalk.common.exception.AdapterException;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.filter.StringFilter;
import com.cheems.pizzatalk.common.service.QueryService;
import com.cheems.pizzatalk.common.specification.SpecificationUtils;
import com.cheems.pizzatalk.entities.ChatMessageEntity_;
import com.cheems.pizzatalk.entities.ConversationEntity;
import com.cheems.pizzatalk.entities.ConversationEntity_;
import com.cheems.pizzatalk.entities.ParticipantEntity;
import com.cheems.pizzatalk.entities.UserEntity;
import com.cheems.pizzatalk.entities.UserEntity_;
import com.cheems.pizzatalk.entities.enumeration.ParticipantStatus;
import com.cheems.pizzatalk.entities.mapper.ChatMessageMapper;
import com.cheems.pizzatalk.entities.mapper.ConversationMapper;
import com.cheems.pizzatalk.entities.mapper.UserMapper;
import com.cheems.pizzatalk.modules.conversation.application.port.in.query.ConversationCriteria;
import com.cheems.pizzatalk.modules.conversation.application.port.out.QueryConversationPort;
import com.cheems.pizzatalk.modules.conversation.domain.Conversation;
import com.cheems.pizzatalk.repository.ConversationRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class QueryConversationAdapter extends QueryService<ConversationEntity> implements QueryConversationPort {

    private final ConversationRepository conversationRepository;

    private final ConversationMapper conversationMapper;

    private final UserMapper userMapper;

    private final ChatMessageMapper chatMessageMapper;

    public QueryConversationAdapter(
        ConversationRepository conversationRepository,
        ConversationMapper conversationMapper,
        UserMapper userMapper,
        ChatMessageMapper chatMessageMapper
    ) {
        this.conversationRepository = conversationRepository;
        this.conversationMapper = conversationMapper;
        this.userMapper = userMapper;
        this.chatMessageMapper = chatMessageMapper;
    }

    @Override
    public Optional<Conversation> findByCriteria(ConversationCriteria criteria) {
        List<ConversationEntity> conversationEntities = conversationRepository.findAll(createSpecification(criteria));
        if (CollectionUtils.isEmpty(conversationEntities)) {
            return Optional.empty();
        }
        Integer conversationEntitiesSize = conversationEntities.size();
        if (conversationEntitiesSize > 1) {
            Set<Long> conversationIds = conversationEntities.stream().map(ConversationEntity::getId).collect(Collectors.toSet());
            throw new AdapterException("Found more than one conversation: " + conversationIds);
        }
        return Optional
            .of(conversationEntities.get(0))
            .map(conversationEntity -> toDomainModel(conversationEntity, criteria.getFetchAttributes()));
    }

    @Override
    public List<Conversation> findListByCriteria(ConversationCriteria criteria) {
        return conversationRepository
            .findAll(createSpecification(criteria))
            .stream()
            .map(conversationEntity -> toDomainModel(conversationEntity, criteria.getFetchAttributes()))
            .collect(Collectors.toList());
    }

    @Override
    public Page<Conversation> findPageByCriteria(ConversationCriteria criteria, Pageable pageable) {
        Page<ConversationEntity> conversationEntitiesPage = conversationRepository.findAll(createSpecification(criteria), pageable);
        return new PageImpl<>(
            conversationMapper.toDomain(conversationEntitiesPage.getContent()),
            pageable,
            conversationEntitiesPage.getTotalElements()
        );
    }

    private Specification<ConversationEntity> createSpecification(ConversationCriteria criteria) {
        Set<String> fetchAttributes = criteria.getFetchAttributes();
        Specification<ConversationEntity> specification = Specification.where(null);

        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ConversationEntity_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), ConversationEntity_.type));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecificationFindByUserId(criteria.getUserId()));
            }
            if (criteria.getUsername() != null) {
                specification = specification.and(buildSpecificationFindByUsername(criteria.getUsername()));
            }
            if (criteria.getChatMessageId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getChatMessageId(),
                            root -> root.join(ConversationEntity_.chatMessages, JoinType.LEFT).get(ChatMessageEntity_.id)
                        )
                    );
            }
            if (!CollectionUtils.isEmpty(fetchAttributes)) {
                specification =
                    specification.and(SpecificationUtils.fetchAttributes(conversationMapper.toEntityAttributes(fetchAttributes)));
            }
            specification = specification.and(SpecificationUtils.distinct(true));
        }
        return specification;
    }

    private Specification<ConversationEntity> buildSpecificationFindByUserId(RangeFilter<Long> userId) {
        if (userId.getEquals() != null) {
            return (root, query, builder) -> {
                Join<ConversationEntity, ParticipantEntity> joinParticipant = SpecificationUtils.getJoinFetch(
                    root,
                    "participants",
                    JoinType.LEFT,
                    false
                );
                Join<ParticipantEntity, UserEntity> joinUser = SpecificationUtils.getJoinFetch(
                    joinParticipant,
                    "user",
                    JoinType.LEFT,
                    false
                );
                return builder.equal(joinUser.get(UserEntity_.id), userId.getEquals());
            };
        }
        if (userId.getIn() != null) {
            return (root, query, builder) -> {
                Join<ConversationEntity, ParticipantEntity> joinParticipant = SpecificationUtils.getJoinFetch(
                    root,
                    "participants",
                    JoinType.LEFT,
                    false
                );
                Join<ParticipantEntity, UserEntity> joinUser = SpecificationUtils.getJoinFetch(
                    joinParticipant,
                    "user",
                    JoinType.LEFT,
                    false
                );
                return joinUser.get(UserEntity_.id).in(userId.getIn());
            };
        }
        return null;
    }

    private Specification<ConversationEntity> buildSpecificationFindByUsername(StringFilter username) {
        if (username.getEquals() != null) {
            return (root, query, builder) -> {
                Join<ConversationEntity, ParticipantEntity> joinParticipant = SpecificationUtils.getJoinFetch(
                    root,
                    "participants",
                    JoinType.LEFT,
                    false
                );
                Join<ParticipantEntity, UserEntity> joinUser = SpecificationUtils.getJoinFetch(
                    joinParticipant,
                    "user",
                    JoinType.LEFT,
                    false
                );
                return builder.equal(joinUser.get(UserEntity_.id), username.getEquals());
            };
        }
        if (username.getIn() != null) {
            return (root, query, builder) -> {
                Join<ConversationEntity, ParticipantEntity> joinParticipant = SpecificationUtils.getJoinFetch(
                    root,
                    "participants",
                    JoinType.LEFT,
                    false
                );
                Join<ParticipantEntity, UserEntity> joinUser = SpecificationUtils.getJoinFetch(
                    joinParticipant,
                    "user",
                    JoinType.LEFT,
                    false
                );
                return joinUser.get(UserEntity_.id).in(username.getIn());
            };
        }
        return null;
    }

    private Conversation toDomainModel(ConversationEntity conversationEntity, Set<String> domainAttributes) {
        Conversation conversation = conversationMapper.toDomain(conversationEntity);
        return enrichConversationDomain(conversation, conversationEntity, domainAttributes);
    }

    private Conversation enrichConversationDomain(
        Conversation conversation,
        ConversationEntity conversationEntity,
        Set<String> domainAttributes
    ) {
        if (CollectionUtils.isEmpty(domainAttributes)) {
            return conversation;
        }

        if (domainAttributes.contains(ConversationMapper.DOMAIN_USER)) {
            conversation.setUsers(
                conversationEntity
                    .getParticipants()
                    .stream()
                    .filter(participantEntity -> participantEntity.getStatus().equals(ParticipantStatus.JOINED))
                    .map(participantEntity -> userMapper.toDomain(participantEntity.getUser()))
                    .collect(Collectors.toSet())
            );
        }
        if (domainAttributes.contains(ConversationMapper.DOMAIN_CHAT_MESSAGES)) {
            conversation.setChatMessages(
                conversationEntity
                    .getChatMessages()
                    .stream()
                    .map(chatMessageEntity -> chatMessageMapper.toDomain(chatMessageEntity))
                    .collect(Collectors.toSet())
            );
        }
        return conversation;
    }
}
