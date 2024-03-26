package com.cheems.pizzatalk.modules.conversation.application.port.in.command;

import com.cheems.pizzatalk.common.cqrs.CommandSelfValidating;
import com.cheems.pizzatalk.entities.enumeration.ConversationType;
import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class CreateConversationCommand extends CommandSelfValidating<CreateConversationCommand> {

    @NotNull
    private ConversationType type;

    private Set<String> usernames;

    private Long inviterId;
}
