package com.cheems.pizzatalk.modules.participant.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.modules.participant.application.port.in.query.ParticipantCriteria;
import com.cheems.pizzatalk.modules.participant.application.port.in.share.QueryParticipantUseCase;
import com.cheems.pizzatalk.modules.participant.application.port.out.QueryParticipantPort;
import com.cheems.pizzatalk.modules.participant.domain.Participant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class QueryParticipantService implements QueryParticipantUseCase {

    private QueryParticipantPort queryParticipantPort;

    public QueryParticipantService(QueryParticipantPort queryParticipantPort) {
        this.queryParticipantPort = queryParticipantPort;
    }

    @Override
    public Optional<Participant> findById(Long id, String... fetchAttributes) {
        ParticipantCriteria criteria = new ParticipantCriteria();

        RangeFilter<Long> idFilter = new RangeFilter<>();
        idFilter.setEquals(id);
        criteria.setId(idFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }

        return findByCriteria(criteria);
    }

    @Override
    public Participant getById(Long id, String... fetchAttributes) {
        return findById(id, fetchAttributes).orElseThrow(() -> new BusinessException("Not found participant with id: " + id));
    }

    @Override
    public Optional<Participant> findByUserIdAndConversationId(Long userId, Long conversationId, String... fetchAttributes) {
        ParticipantCriteria criteria = new ParticipantCriteria();

        RangeFilter<Long> userIdFilter = new RangeFilter<>();
        userIdFilter.setEquals(userId);

        RangeFilter<Long> conversationIdFilter = new RangeFilter<>();
        conversationIdFilter.setEquals(conversationId);

        criteria.setUserId(userIdFilter);
        criteria.setConversationId(conversationIdFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }

        return findByCriteria(criteria);
    }

    @Override
    public Participant getByUserIdAndConversationId(Long userId, Long conversationId, String... fetchAttributes) {
        return findByUserIdAndConversationId(userId, conversationId, fetchAttributes)
            .orElseThrow(() ->
                new BusinessException("Not found participant with user id: " + userId + " and conversation id: " + conversationId)
            );
    }

    @Override
    public Optional<Participant> findByCriteria(ParticipantCriteria criteria) {
        return queryParticipantPort.findByCriteria(criteria);
    }

    @Override
    public Participant getByCriteria(ParticipantCriteria criteria) {
        return findByCriteria(criteria)
            .orElseThrow(() -> new BusinessException("Not found participant with criteria" + criteria.toString()));
    }

    @Override
    public List<Participant> findListByCriteria(ParticipantCriteria criteria) {
        return queryParticipantPort.findListByCriteria(criteria);
    }

    @Override
    public Page<Participant> findPageByCriteria(ParticipantCriteria criteria, Pageable pageable) {
        return queryParticipantPort.findPageByCriteria(criteria, pageable);
    }
}
