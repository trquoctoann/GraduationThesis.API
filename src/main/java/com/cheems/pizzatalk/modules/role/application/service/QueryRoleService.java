package com.cheems.pizzatalk.modules.role.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.filter.StringFilter;
import com.cheems.pizzatalk.modules.role.application.port.in.query.RoleCriteria;
import com.cheems.pizzatalk.modules.role.application.port.in.share.QueryRoleUseCase;
import com.cheems.pizzatalk.modules.role.application.port.out.QueryRolePort;
import com.cheems.pizzatalk.modules.role.domain.Role;
import java.util.List;
import java.util.Optional;
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
    public Optional<Role> findById(Long id) {
        RoleCriteria criteria = new RoleCriteria();

        RangeFilter<Long> idFilter = new RangeFilter<Long>();
        idFilter.setEquals(id);
        criteria.setId(idFilter);

        return findByCriteria(criteria);
    }

    @Override
    public Role getById(Long id) {
        return findById(id).orElseThrow(() -> new BusinessException("Not found role with id: " + id));
    }

    @Override
    public Optional<Role> findByAuthority(String authority) {
        RoleCriteria criteria = new RoleCriteria();

        StringFilter authorityFilter = new StringFilter();
        authorityFilter.setEquals(authority);
        criteria.setAuthority(authorityFilter);

        return findByCriteria(criteria);
    }

    @Override
    public Role getByAuthority(String authority) {
        return findByAuthority(authority).orElseThrow(() -> new BusinessException("Not found role with authority: " + authority));
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
}
