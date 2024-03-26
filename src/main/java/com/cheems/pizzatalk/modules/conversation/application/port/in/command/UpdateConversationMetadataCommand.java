package com.cheems.pizzatalk.modules.conversation.application.port.in.command;

import com.cheems.pizzatalk.common.cqrs.CommandSelfValidating;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class UpdateConversationMetadataCommand extends CommandSelfValidating<UpdateConversationMetadataCommand> {

    @NotNull
    private Long id;

    private String title;

    private String avatar;
}
