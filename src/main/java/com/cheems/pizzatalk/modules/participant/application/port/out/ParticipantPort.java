package com.cheems.pizzatalk.modules.participant.application.port.out;

import com.cheems.pizzatalk.modules.participant.domain.Participant;

public interface ParticipantPort {
    Participant save(Participant participant);

    void deleteById(Long id);
}
