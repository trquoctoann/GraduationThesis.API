package com.cheems.pizzatalk.modules.participant.application.port.in.share;

import com.cheems.pizzatalk.modules.participant.application.port.in.query.ParticipantCriteria;
import com.cheems.pizzatalk.modules.participant.domain.Participant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryParticipantUseCase {
    Optional<Participant> findById(Long id, String... fetchAttributes);

    Participant getById(Long id, String... fetchAttributes);

    Optional<Participant> findByUserIdAndConversationId(Long userId, Long conversationId, String... fetchAttributes);

    Participant getByUserIdAndConversationId(Long userId, Long conversationId, String... fetchAttributes);

    Optional<Participant> findByCriteria(ParticipantCriteria criteria);

    Participant getByCriteria(ParticipantCriteria criteria);

    List<Participant> findListByCriteria(ParticipantCriteria criteria);

    Page<Participant> findPageByCriteria(ParticipantCriteria criteria, Pageable pageable);
}
