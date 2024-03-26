package com.cheems.pizzatalk.modules.user.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.filter.StringFilter;
import com.cheems.pizzatalk.modules.user.application.port.in.query.UserCriteria;
import com.cheems.pizzatalk.modules.user.application.port.in.share.QueryUserUseCase;
import com.cheems.pizzatalk.modules.user.application.port.out.QueryUserPort;
import com.cheems.pizzatalk.modules.user.domain.User;
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
public class QueryUserService implements QueryUserUseCase {

    private final QueryUserPort queryUserPort;

    public QueryUserService(QueryUserPort queryUserPort) {
        this.queryUserPort = queryUserPort;
    }

    @Override
    public Optional<User> findById(Long id, String... fetchAttributes) {
        UserCriteria criteria = new UserCriteria();

        RangeFilter<Long> idFilter = new RangeFilter<Long>();
        idFilter.setEquals(id);
        criteria.setId(idFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }

        return findByCriteria(criteria);
    }

    @Override
    public User getById(Long id, String... fetchAttributes) {
        return findById(id, fetchAttributes).orElseThrow(() -> new BusinessException("Not found user with id: " + id));
    }

    @Override
    public Optional<User> findByUsername(String username, String... fetchAttributes) {
        UserCriteria criteria = new UserCriteria();

        StringFilter usernameFilter = new StringFilter();
        usernameFilter.setEquals(username);
        criteria.setUsername(usernameFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }
        return findByCriteria(criteria);
    }

    @Override
    public User getByUsername(String username, String... fetchAttributes) {
        return findByUsername(username, fetchAttributes)
            .orElseThrow(() -> new BusinessException("Not found user with username: " + username));
    }

    @Override
    public Optional<User> findByEmail(String email, String... fetchAttributes) {
        UserCriteria criteria = new UserCriteria();

        StringFilter emailFilter = new StringFilter();
        emailFilter.setEquals(email);
        criteria.setEmail(emailFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }

        return findByCriteria(criteria);
    }

    @Override
    public User getByEmail(String email, String... fetchAttributes) {
        return findByEmail(email, fetchAttributes).orElseThrow(() -> new BusinessException("Not found user with email: " + email));
    }

    @Override
    public Optional<User> findByCriteria(UserCriteria criteria) {
        return queryUserPort.findByCriteria(criteria);
    }

    @Override
    public User getByCriteria(UserCriteria criteria) {
        return findByCriteria(criteria).orElseThrow(() -> new BusinessException("Not found user with criteria" + criteria.toString()));
    }

    @Override
    public List<User> findListByCriteria(UserCriteria criteria) {
        return queryUserPort.findListByCriteria(criteria);
    }

    @Override
    public Page<User> findPageByCriteria(UserCriteria criteria, Pageable pageable) {
        return queryUserPort.findPageByCriteria(criteria, pageable);
    }

    @Override
    public List<User> findListByListUsernames(List<String> usernames) {
        UserCriteria criteria = new UserCriteria();

        StringFilter usernameFilter = new StringFilter();
        usernameFilter.setIn(new ArrayList<>(usernames));

        criteria.setUsername(usernameFilter);
        return findListByCriteria(criteria);
    }
}
