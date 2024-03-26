package com.cheems.pizzatalk.modules.participant.application.port.in.command;

import com.cheems.pizzatalk.common.cqrs.CommandSelfValidating;
import com.cheems.pizzatalk.entities.enumeration.ParticipantStatus;
import java.time.Instant;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class CreateParticipantCommand extends CommandSelfValidating<CreateParticipantCommand> {

    private ParticipantStatus status;

    private Instant joinedAt;

    @NotNull
    private Long userId;

    @NotNull
    private Long conversationId;
}
