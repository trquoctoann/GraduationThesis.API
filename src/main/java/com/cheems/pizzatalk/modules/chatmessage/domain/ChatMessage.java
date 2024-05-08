package com.cheems.pizzatalk.modules.chatmessage.domain;

import com.cheems.pizzatalk.entities.enumeration.ChatMessageStatus;
import com.cheems.pizzatalk.entities.enumeration.ChatMessageType;
import com.cheems.pizzatalk.modules.attachment.domain.Attachment;
import com.cheems.pizzatalk.modules.conversation.domain.Conversation;
import com.cheems.pizzatalk.modules.participant.domain.Participant;
import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ChatMessage implements Serializable {

    private Long id;

    private String content;

    @NotNull
    private ChatMessageType type;

    @NotNull
    private ChatMessageStatus status;

    @NotNull
    private Instant sentTime;

    private Instant readTime;

    @NotNull
    private Long conversationId;

    private Conversation conversation;

    @NotNull
    private Long participantId;

    private Participant participant;

    private Set<Attachment> attachments;
}
