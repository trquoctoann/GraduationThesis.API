package com.cheems.pizzatalk.modules.participant.application.port.in.share;

import com.cheems.pizzatalk.modules.participant.application.port.in.command.CreateParticipantCommand;
import com.cheems.pizzatalk.modules.participant.domain.Participant;

public interface ParticipantLifecycleUseCase {
    Participant create(CreateParticipantCommand command);

    Participant joinConversation(Long userId, Long conversationId);

    Participant leftConversation(Long userId, Long conversationId);

    Participant deleteConversation(Long userId, Long conversationId);

    void deleteById(Long id);
}
