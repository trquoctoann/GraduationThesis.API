package com.cheems.pizzatalk.modules.conversation.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.filter.StringFilter;
import com.cheems.pizzatalk.entities.enumeration.ConversationType;
import com.cheems.pizzatalk.entities.filter.ConversationTypeFilter;
import com.cheems.pizzatalk.modules.conversation.application.port.in.query.ConversationCriteria;
import com.cheems.pizzatalk.modules.conversation.application.port.in.share.QueryConversationUseCase;
import com.cheems.pizzatalk.modules.conversation.application.port.out.QueryConversationPort;
import com.cheems.pizzatalk.modules.conversation.domain.Conversation;
import java.util.ArrayList;
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
public class QueryConversationService implements QueryConversationUseCase {

    private final QueryConversationPort queryConversationPort;

    public QueryConversationService(QueryConversationPort queryConversationPort) {
        this.queryConversationPort = queryConversationPort;
    }

    @Override
    public Optional<Conversation> findById(Long id, String... fetchAttributes) {
        ConversationCriteria criteria = new ConversationCriteria();

        RangeFilter<Long> idFilter = new RangeFilter<>();
        idFilter.setEquals(id);
        criteria.setId(idFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }
        return findByCriteria(criteria);
    }

    @Override
    public Conversation getById(Long id, String... fetchAttributes) {
        return findById(id, fetchAttributes).orElseThrow(() -> new BusinessException("Not found conversation with id: " + id));
    }

    @Override
    public Optional<Conversation> findByCriteria(ConversationCriteria criteria) {
        return queryConversationPort.findByCriteria(criteria);
    }

    @Override
    public Conversation getByCriteria(ConversationCriteria criteria) {
        return findByCriteria(criteria)
            .orElseThrow(() -> new BusinessException("Not found conversation with criteria" + criteria.toString()));
    }

    @Override
    public List<Conversation> findListByCriteria(ConversationCriteria criteria) {
        return queryConversationPort.findListByCriteria(criteria);
    }

    @Override
    public Page<Conversation> findPageByCriteria(ConversationCriteria criteria, Pageable pageable) {
        return queryConversationPort.findPageByCriteria(criteria, pageable);
    }

    @Override
    public List<Conversation> findListPrivateByUsernames(List<String> usernames) {
        ConversationCriteria criteria = new ConversationCriteria();

        ConversationTypeFilter typeFilter = new ConversationTypeFilter();
        typeFilter.setEquals(ConversationType.PRIVATE);

        StringFilter usernameFilter = new StringFilter();
        usernameFilter.setIn(new ArrayList<>(usernames));

        criteria.setType(typeFilter);
        criteria.setUsername(usernameFilter);

        return findListByCriteria(criteria);
    }

    @Override
    public List<Conversation> findListByUserId(Long userId) {
        ConversationCriteria criteria = new ConversationCriteria();

        RangeFilter<Long> userIdFilter = new RangeFilter<>();
        userIdFilter.setEquals(userId);
        criteria.setUserId(userIdFilter);

        return findListByCriteria(criteria);
    }
}
