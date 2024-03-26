package com.cheems.pizzatalk.modules.participant.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.entities.enumeration.ConversationType;
import com.cheems.pizzatalk.entities.enumeration.ParticipantStatus;
import com.cheems.pizzatalk.entities.mapper.ParticipantMapper;
import com.cheems.pizzatalk.modules.conversation.application.port.in.share.ConversationLifecycleUseCase;
import com.cheems.pizzatalk.modules.participant.application.port.in.command.CreateParticipantCommand;
import com.cheems.pizzatalk.modules.participant.application.port.in.share.ParticipantLifecycleUseCase;
import com.cheems.pizzatalk.modules.participant.application.port.in.share.QueryParticipantUseCase;
import com.cheems.pizzatalk.modules.participant.application.port.out.ParticipantPort;
import com.cheems.pizzatalk.modules.participant.domain.Participant;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ParticipantLifecycleService implements ParticipantLifecycleUseCase {

    private static final Logger log = LoggerFactory.getLogger(ParticipantLifecycleService.class);

    private final ObjectMapper objectMapper;

    private final ParticipantPort participantPort;

    private final ConversationLifecycleUseCase conversationLifecycleUseCase;

    private final QueryParticipantUseCase queryParticipantUseCase;

    public ParticipantLifecycleService(
        ObjectMapper objectMapper,
        ParticipantPort participantPort,
        ConversationLifecycleUseCase conversationLifecycleUseCase,
        QueryParticipantUseCase queryParticipantUseCase
    ) {
        this.objectMapper = objectMapper;
        this.participantPort = participantPort;
        this.conversationLifecycleUseCase = conversationLifecycleUseCase;
        this.queryParticipantUseCase = queryParticipantUseCase;
    }

    @Override
    public Participant create(CreateParticipantCommand command) {
        log.debug("Creating participant: {}", command);
        if (queryParticipantUseCase.findByUserIdAndConversationId(command.getUserId(), command.getConversationId()).isPresent()) {
            throw new BusinessException("User is already a participant or invited");
        }
        Participant participant = objectMapper.convertValue(command, Participant.class);
        participant = participantPort.save(participant);

        log.debug("Created participant: {}", command);
        return participant;
    }

    @Override
    public Participant joinConversation(Long userId, Long conversationId) {
        log.debug("User id: {} joining conversation: {}", userId, conversationId);
        Participant participant = queryParticipantUseCase.getByUserIdAndConversationId(userId, conversationId);
        if (participant.getStatus().equals(ParticipantStatus.JOINED)) {
            throw new BusinessException("User has joined this conversation");
        }
        participant.setStatus(ParticipantStatus.JOINED);
        participant.setJoinedAt(Instant.now());

        participant = participantPort.save(participant);
        conversationLifecycleUseCase.addUserToConversation(userId, conversationId);
        log.debug("User id: {} joined conversation: {}", userId, conversationId);
        return participant;
    }

    @Override
    public Participant leftConversation(Long userId, Long conversationId) {
        log.debug("User id: {} leaving conversation: {}", userId, conversationId);
        Participant participant = queryParticipantUseCase.getByUserIdAndConversationId(
            userId,
            conversationId,
            ParticipantMapper.DOMAIN_CONVERSATION
        );
        if (!participant.getStatus().equals(ParticipantStatus.JOINED)) {
            throw new BusinessException("User is not in conversation");
        } else if (!participant.getConversation().getType().equals(ConversationType.GROUP)) {
            throw new BusinessException("User can only leave group type conversation");
        }
        participant.setStatus(ParticipantStatus.LEFT);
        participant.setLeftAt(Instant.now());

        participant = participantPort.save(participant);
        conversationLifecycleUseCase.removeUserOfConversation(userId, conversationId);
        log.debug("User id: {} left conversation: {}", userId, conversationId);
        return participant;
    }

    @Override
    public Participant deleteConversation(Long userId, Long conversationId) {
        log.debug("User id: {} deleting all messages of conversation id: {}", userId, conversationId);
        Participant participant = queryParticipantUseCase.getByUserIdAndConversationId(userId, conversationId);
        if (!participant.getStatus().equals(ParticipantStatus.JOINED)) {
            throw new BusinessException("User is not in conversation");
        }
        participant.setDeletedAt(Instant.now());

        participant = participantPort.save(participant);
        log.debug("User id: {} deleted all messages of conversation id: {}", userId, conversationId);
        return participant;
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Deleting participant, id: {}", id);
        participantPort.deleteById(id);
        log.debug("Deleted participant, id: {}", id);
    }
}
