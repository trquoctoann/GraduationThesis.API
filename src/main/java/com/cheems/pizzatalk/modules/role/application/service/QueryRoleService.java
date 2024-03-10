package com.cheems.pizzatalk.modules.role.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.filter.StringFilter;
import com.cheems.pizzatalk.modules.role.application.port.in.query.RoleCriteria;
import com.cheems.pizzatalk.modules.role.application.port.in.share.QueryRoleUseCase;
import com.cheems.pizzatalk.modules.role.application.port.out.QueryRolePort;
import com.cheems.pizzatalk.modules.role.domain.Role;
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
public class QueryRoleService implements QueryRoleUseCase {

    private final QueryRolePort queryRolePort;

    public QueryRoleService(QueryRolePort queryRolePort) {
        this.queryRolePort = queryRolePort;
    }

    @Override
    public Optional<Role> findById(Long id, String... fetchAttributes) {
        RoleCriteria criteria = new RoleCriteria();

        RangeFilter<Long> idFilter = new RangeFilter<Long>();
        idFilter.setEquals(id);
        criteria.setId(idFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }

        return findByCriteria(criteria);
    }

    @Override
    public Role getById(Long id, String... fetchAttributes) {
        return findById(id, fetchAttributes).orElseThrow(() -> new BusinessException("Not found role with id: " + id));
    }

    @Override
    public Optional<Role> findByName(String name, String... fetchAttributes) {
        RoleCriteria criteria = new RoleCriteria();

        StringFilter nameFilter = new StringFilter();
        nameFilter.setEquals(name);
        criteria.setName(nameFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }

        return findByCriteria(criteria);
    }

    @Override
    public Role getByName(String name, String... fetchAttributes) {
        return findByName(name, fetchAttributes).orElseThrow(() -> new BusinessException("Not found role with name: " + name));
    }

    @Override
    public Optional<Role> findByCriteria(RoleCriteria criteria) {
        return queryRolePort.findByCriteria(criteria);
    }

    @Override
    public List<Role> findListByCriteria(RoleCriteria criteria) {
        return queryRolePort.findListByCriteria(criteria);
    }

    @Override
    public Page<Role> findPageByCriteria(RoleCriteria criteria, Pageable pageable) {
        return queryRolePort.findPageByCriteria(criteria, pageable);
    }

    @Override
    public List<Role> findListByListName(List<String> names) {
        RoleCriteria criteria = new RoleCriteria();

        StringFilter nameFilter = new StringFilter();
        nameFilter.setIn(new ArrayList<>(names));
        criteria.setName(nameFilter);

        return findListByCriteria(criteria);
    }

    @Override
    public List<Role> findListByUserId(Long userId) {
        RoleCriteria criteria = new RoleCriteria();

        RangeFilter<Long> userIdFilter = new RangeFilter<>();
        userIdFilter.setEquals(userId);
        criteria.setUserId(userIdFilter);

        return findListByCriteria(criteria);
    }
}
