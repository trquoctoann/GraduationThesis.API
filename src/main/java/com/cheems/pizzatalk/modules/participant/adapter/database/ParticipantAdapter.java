package com.cheems.pizzatalk.modules.participant.adapter.database;

import com.cheems.pizzatalk.entities.mapper.ParticipantMapper;
import com.cheems.pizzatalk.modules.participant.application.port.out.ParticipantPort;
import com.cheems.pizzatalk.modules.participant.domain.Participant;
import com.cheems.pizzatalk.repository.ParticipantRepository;
import org.springframework.stereotype.Component;

@Component
public class ParticipantAdapter implements ParticipantPort {

    private final ParticipantRepository participantRepository;

    private final ParticipantMapper participantMapper;

    public ParticipantAdapter(ParticipantRepository participantRepository, ParticipantMapper participantMapper) {
        this.participantRepository = participantRepository;
        this.participantMapper = participantMapper;
    }

    @Override
    public Participant save(Participant participant) {
        return participantMapper.toDomain(participantRepository.save(participantMapper.toEntity(participant)));
    }

    @Override
    public void deleteById(Long id) {
        participantRepository.deleteById(id);
    }
}
