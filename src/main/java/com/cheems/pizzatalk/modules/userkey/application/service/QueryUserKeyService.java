package com.cheems.pizzatalk.modules.userkey.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.filter.StringFilter;
import com.cheems.pizzatalk.entities.enumeration.UserKeyStatus;
import com.cheems.pizzatalk.entities.enumeration.UserKeyType;
import com.cheems.pizzatalk.entities.filter.UserKeyStatusFilter;
import com.cheems.pizzatalk.entities.filter.UserKeyTypeFilter;
import com.cheems.pizzatalk.modules.userkey.application.port.in.query.UserKeyCriteria;
import com.cheems.pizzatalk.modules.userkey.application.port.in.share.QueryUserKeyUseCase;
import com.cheems.pizzatalk.modules.userkey.application.port.out.QueryUserKeyPort;
import com.cheems.pizzatalk.modules.userkey.domain.UserKey;
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
public class QueryUserKeyService implements QueryUserKeyUseCase {

    private final QueryUserKeyPort queryUserKeyPort;

    public QueryUserKeyService(QueryUserKeyPort queryUserKeyPort) {
        this.queryUserKeyPort = queryUserKeyPort;
    }

    @Override
    public Optional<UserKey> findById(Long id, String... fetchAttributes) {
        UserKeyCriteria criteria = new UserKeyCriteria();

        RangeFilter<Long> idFilter = new RangeFilter<>();
        idFilter.setEquals(id);
        criteria.setId(idFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }

        return findByCriteria(criteria);
    }

    @Override
    public UserKey getById(Long id, String... fetchAttributes) {
        return findById(id, fetchAttributes).orElseThrow(() -> new BusinessException("Not found user key with id: " + id));
    }

    @Override
    public Optional<UserKey> findByValue(String value, String... fetchAttributes) {
        UserKeyCriteria criteria = new UserKeyCriteria();

        StringFilter valueFilter = new StringFilter();
        valueFilter.setEquals(value);
        criteria.setValue(valueFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }

        return findByCriteria(criteria);
    }

    @Override
    public UserKey getByValue(String value, String... fetchAttributes) {
        return findByValue(value, fetchAttributes).orElseThrow(() -> new BusinessException("Not found user key with value: " + value));
    }

    @Override
    public Optional<UserKey> findByCriteria(UserKeyCriteria criteria) {
        return queryUserKeyPort.findByCriteria(criteria);
    }

    @Override
    public UserKey getByCriteria(UserKeyCriteria criteria) {
        return findByCriteria(criteria).orElseThrow(() -> new BusinessException("Not found user key with criteria: " + criteria));
    }

    @Override
    public List<UserKey> findListByCriteria(UserKeyCriteria criteria) {
        return queryUserKeyPort.findListByCriteria(criteria);
    }

    @Override
    public Page<UserKey> findPageByCriteria(UserKeyCriteria criteria, Pageable pageable) {
        return queryUserKeyPort.findPageByCriteria(criteria, pageable);
    }

    @Override
    public List<UserKey> findActiveKeyByUserId(UserKeyType type, Long userId) {
        UserKeyCriteria criteria = new UserKeyCriteria();

        RangeFilter<Long> userIdFilter = new RangeFilter<>();
        userIdFilter.setEquals(userId);

        UserKeyTypeFilter userKeyTypeFilter = new UserKeyTypeFilter();
        userKeyTypeFilter.setEquals(type);

        UserKeyStatusFilter userKeyStatusFilter = new UserKeyStatusFilter();
        userKeyStatusFilter.setEquals(UserKeyStatus.ACTIVE);

        criteria.setUserId(userIdFilter);
        criteria.setType(userKeyTypeFilter);
        criteria.setStatus(userKeyStatusFilter);

        return queryUserKeyPort.findListByCriteria(criteria);
    }
}
