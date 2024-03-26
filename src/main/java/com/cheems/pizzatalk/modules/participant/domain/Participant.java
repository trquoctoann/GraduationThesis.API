package com.cheems.pizzatalk.modules.participant.domain;

import com.cheems.pizzatalk.entities.enumeration.ParticipantStatus;
import com.cheems.pizzatalk.modules.chatmessage.domain.ChatMessage;
import com.cheems.pizzatalk.modules.conversation.domain.Conversation;
import com.cheems.pizzatalk.modules.user.domain.User;
import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Participant implements Serializable {

    private Long id;

    @NotNull
    private ParticipantStatus status;

    private Instant joinedAt;

    private Instant leftAt;

    private Instant deletedAt;

    @NotNull
    private Long userId;

    private User user;

    @NotNull
    private Long conversationId;

    private Conversation conversation;

    private Set<ChatMessage> chatMessages;
}
