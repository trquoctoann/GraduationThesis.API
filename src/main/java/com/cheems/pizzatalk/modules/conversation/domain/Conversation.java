package com.cheems.pizzatalk.modules.conversation.domain;

import com.cheems.pizzatalk.entities.enumeration.ConversationType;
import com.cheems.pizzatalk.modules.chatmessage.domain.ChatMessage;
import com.cheems.pizzatalk.modules.user.domain.User;
import java.io.Serializable;
import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Conversation implements Serializable {

    private Long id;

    @NotNull
    private ConversationType type;

    private String metadata;

    private Set<User> users;

    private Set<ChatMessage> chatMessages;
}
