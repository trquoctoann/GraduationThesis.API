package com.cheems.pizzatalk.modules.participant.application.port.out;

import com.cheems.pizzatalk.modules.participant.application.port.in.query.ParticipantCriteria;
import com.cheems.pizzatalk.modules.participant.domain.Participant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryParticipantPort {
    Optional<Participant> findByCriteria(ParticipantCriteria criteria);

    List<Participant> findListByCriteria(ParticipantCriteria criteria);

    Page<Participant> findPageByCriteria(ParticipantCriteria criteria, Pageable pageable);
}
