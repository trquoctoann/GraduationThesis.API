package com.cheems.pizzatalk.modules.conversation.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.entities.enumeration.ConversationType;
import com.cheems.pizzatalk.entities.enumeration.ParticipantStatus;
import com.cheems.pizzatalk.modules.conversation.application.port.in.command.CreateConversationCommand;
import com.cheems.pizzatalk.modules.conversation.application.port.in.command.UpdateConversationMetadataCommand;
import com.cheems.pizzatalk.modules.conversation.application.port.in.share.ConversationLifecycleUseCase;
import com.cheems.pizzatalk.modules.conversation.application.port.in.share.QueryConversationUseCase;
import com.cheems.pizzatalk.modules.conversation.application.port.out.ConversationPort;
import com.cheems.pizzatalk.modules.conversation.domain.Conversation;
import com.cheems.pizzatalk.modules.conversation.domain.ConversationMetadata;
import com.cheems.pizzatalk.modules.participant.application.port.in.command.CreateParticipantCommand;
import com.cheems.pizzatalk.modules.participant.application.port.in.share.ParticipantLifecycleUseCase;
import com.cheems.pizzatalk.modules.user.application.port.in.share.QueryUserUseCase;
import com.cheems.pizzatalk.modules.user.domain.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ConversationLifecycleService implements ConversationLifecycleUseCase {

    private static final Logger log = LoggerFactory.getLogger(ConversationLifecycleService.class);

    private final ObjectMapper objectMapper;

    private final ConversationPort conversationPort;

    private final ParticipantLifecycleUseCase participantLifecycleUseCase;

    private final QueryConversationUseCase queryConversationUseCase;

    private final QueryUserUseCase queryUserUseCase;

    public ConversationLifecycleService(
        ObjectMapper objectMapper,
        ConversationPort conversationPort,
        ParticipantLifecycleUseCase participantLifecycleUseCase,
        QueryConversationUseCase queryConversationUseCase,
        QueryUserUseCase queryUserUseCase
    ) {
        this.objectMapper = objectMapper;
        this.conversationPort = conversationPort;
        this.participantLifecycleUseCase = participantLifecycleUseCase;
        this.queryConversationUseCase = queryConversationUseCase;
        this.queryUserUseCase = queryUserUseCase;
    }

    @Override
    public Conversation create(CreateConversationCommand command) {
        log.debug("Creating conversation: {}", command);
        if (command.getType().equals(ConversationType.PRIVATE)) {
            if (command.getUsernames().size() > 2) {
                throw new BusinessException("Private conversation can only include 2 members");
            }

            if (queryConversationUseCase.findListPrivateByUsernames(new ArrayList<>(command.getUsernames())).size() > 1) {
                throw new BusinessException("Conversation already exists");
            }
        }
        Conversation conversation = objectMapper.convertValue(command, Conversation.class);
        conversation = conversationPort.save(conversation);

        List<User> inInviteDemandUser = queryUserUseCase.findListByListUsernames(new ArrayList<>(command.getUsernames()));
        Map<String, String> memberNamesMap = new HashMap<>();
        for (User user : inInviteDemandUser) {
            CreateParticipantCommand createParticipantCommand = new CreateParticipantCommand();
            createParticipantCommand.setUserId(user.getId());
            createParticipantCommand.setConversationId(conversation.getId());
            createParticipantCommand.setStatus(ParticipantStatus.INVITED);

            if (user.getId().equals(command.getInviterId())) {
                createParticipantCommand.setStatus(ParticipantStatus.JOINED);
                createParticipantCommand.setJoinedAt(Instant.now());
                memberNamesMap.put(user.getUsername(), user.getFirstName());
            }
            participantLifecycleUseCase.create(createParticipantCommand);
        }

        String conversationMetadata = buildNewConversationMetadata(memberNamesMap);
        conversation.setMetadata(conversationMetadata);
        conversation = conversationPort.save(conversation);

        log.debug("Created conversation: {}", command);
        return conversation;
    }

    @Override
    public Conversation updateMetadata(UpdateConversationMetadataCommand command) {
        log.debug("Updating metadata of conversation, id: {}", command.getId());
        Conversation existConversation = queryConversationUseCase.getById(command.getId());

        ConversationMetadata conversationMetadata = objectMapper.convertValue(command, ConversationMetadata.class);
        try {
            String newConversationMetadata = objectMapper.writeValueAsString(conversationMetadata);
            existConversation.setMetadata(newConversationMetadata);
            log.debug("Updated metadata of conversation, id: {}", command.getId());
            return conversationPort.save(existConversation);
        } catch (JsonProcessingException e) {
            throw new BusinessException("Error when parse conversation metadata to string!");
        }
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Deleting conversation, id: {}", id);
        log.debug("Deleted conversation, id: {}", id);
    }

    @Override
    public void inviteUserToConversation(Long userId, Long conversationId) {
        log.debug("Inviting user id: {} to conversation id: {}", userId, conversationId);
        Conversation conversation = queryConversationUseCase.getById(conversationId);
        if (!conversation.getType().equals(ConversationType.GROUP)) {
            throw new BusinessException("Conversation type must be group to invite new member");
        }
        CreateParticipantCommand createParticipantCommand = new CreateParticipantCommand();
        createParticipantCommand.setUserId(userId);
        createParticipantCommand.setConversationId(conversationId);
        createParticipantCommand.setStatus(ParticipantStatus.INVITED);

        participantLifecycleUseCase.create(createParticipantCommand);
        log.debug("Invited user id: {} to conversation id: {}", userId, conversationId);
    }

    @Override
    public Conversation addUserToConversation(Long userId, Long conversationId) {
        log.debug("Adding user id: {} to conversation id: {}", userId, conversationId);
        Conversation existConversation = queryConversationUseCase.getById(conversationId);
        User user = queryUserUseCase.getById(userId);

        ConversationMetadata conversationMetadata = objectMapper.convertValue(existConversation.getMetadata(), ConversationMetadata.class);
        Map<String, String> memberNames = conversationMetadata.getMemberNames();
        memberNames.put(user.getUsername(), user.getFirstName());

        Integer countMembers = conversationMetadata.getCountMembers();
        countMembers += 1;

        conversationMetadata.setMemberNames(memberNames);
        conversationMetadata.setCountMembers(countMembers);

        try {
            String newConversationMetadata = objectMapper.writeValueAsString(conversationMetadata);
            existConversation.setMetadata(newConversationMetadata);
            log.debug("Added user id: {} to conversation id: {}", userId, conversationId);
            return conversationPort.save(existConversation);
        } catch (JsonProcessingException e) {
            throw new BusinessException("Error when parse conversation metadata to string!");
        }
    }

    @Override
    public Conversation removeUserOfConversation(Long userId, Long conversationId) {
        log.debug("Removing user id: {} of conversation id: {}", userId, conversationId);
        Conversation existConversation = queryConversationUseCase.getById(conversationId);
        User user = queryUserUseCase.getById(userId);

        ConversationMetadata conversationMetadata = objectMapper.convertValue(existConversation.getMetadata(), ConversationMetadata.class);
        Map<String, String> memberNames = conversationMetadata.getMemberNames();
        memberNames.remove(user.getUsername());

        Integer countMembers = conversationMetadata.getCountMembers();
        countMembers -= 1;

        conversationMetadata.setMemberNames(memberNames);
        conversationMetadata.setCountMembers(countMembers);

        try {
            String newConversationMetadata = objectMapper.writeValueAsString(conversationMetadata);
            existConversation.setMetadata(newConversationMetadata);
            log.debug("Removed user id: {} of conversation id: {}", userId, conversationId);
            return conversationPort.save(existConversation);
        } catch (JsonProcessingException e) {
            throw new BusinessException("Error when parse conversation metadata to string!");
        }
    }

    private String buildNewConversationMetadata(Map<String, String> memberNamesMap) {
        ConversationMetadata conversationMetadata = new ConversationMetadata();

        conversationMetadata.setMemberNames(memberNamesMap);
        conversationMetadata.setCountMembers(memberNamesMap.size());

        try {
            return objectMapper.writeValueAsString(conversationMetadata);
        } catch (JsonProcessingException e) {
            throw new BusinessException("Error when parse conversation metadata to string!");
        }
    }
}
