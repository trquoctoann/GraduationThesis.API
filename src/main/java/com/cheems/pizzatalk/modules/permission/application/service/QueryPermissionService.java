package com.cheems.pizzatalk.modules.permission.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.filter.StringFilter;
import com.cheems.pizzatalk.modules.permission.application.port.in.query.PermissionCriteria;
import com.cheems.pizzatalk.modules.permission.application.port.in.share.QueryPermissionUseCase;
import com.cheems.pizzatalk.modules.permission.application.port.out.QueryPermissionPort;
import com.cheems.pizzatalk.modules.permission.domain.Permission;
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
public class QueryPermissionService implements QueryPermissionUseCase {

    private final QueryPermissionPort queryPermissionPort;

    public QueryPermissionService(QueryPermissionPort queryPermissionPort) {
        this.queryPermissionPort = queryPermissionPort;
    }

    @Override
    public Optional<Permission> findById(Long id, String... fetchAttributes) {
        PermissionCriteria criteria = new PermissionCriteria();

        RangeFilter<Long> idFilter = new RangeFilter<>();
        idFilter.setEquals(id);
        criteria.setId(idFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }

        return findByCriteria(criteria);
    }

    @Override
    public Permission getById(Long id, String... fetchAttributes) {
        return findById(id, fetchAttributes).orElseThrow(() -> new BusinessException("Not found permission with id: " + id));
    }

    @Override
    public Optional<Permission> findByName(String name, String... fetchAttributes) {
        PermissionCriteria criteria = new PermissionCriteria();

        StringFilter nameFilter = new StringFilter();
        nameFilter.setEquals(name);
        criteria.setName(nameFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }

        return findByCriteria(criteria);
    }

    @Override
    public Permission getByName(String name, String... fetchAttributes) {
        return findByName(name, fetchAttributes).orElseThrow(() -> new BusinessException("Not found permission with name: " + name));
    }

    @Override
    public Optional<Permission> findByCriteria(PermissionCriteria criteria) {
        return queryPermissionPort.findByCriteria(criteria);
    }

    @Override
    public Permission getByCriteria(PermissionCriteria criteria) {
        return findByCriteria(criteria)
            .orElseThrow(() -> new BusinessException("Not found permission with criteria: " + criteria.toString()));
    }

    @Override
    public List<Permission> findListByCriteria(PermissionCriteria criteria) {
        return queryPermissionPort.findListByCriteria(criteria);
    }

    @Override
    public Page<Permission> findPageByCriteria(PermissionCriteria criteria, Pageable pageable) {
        return queryPermissionPort.findPageByCriteria(criteria, pageable);
    }

    @Override
    public List<Permission> findListByListPermissionNames(List<String> permissionNames) {
        PermissionCriteria criteria = new PermissionCriteria();

        StringFilter nameFilter = new StringFilter();
        nameFilter.setIn(new ArrayList<>(permissionNames));
        criteria.setName(nameFilter);

        return findListByCriteria(criteria);
    }

    @Override
    public List<Permission> findListByRoleId(Long roleId) {
        PermissionCriteria criteria = new PermissionCriteria();

        RangeFilter<Long> roleIdFilter = new RangeFilter<>();
        roleIdFilter.setEquals(roleId);
        criteria.setRoleId(roleIdFilter);

        return findListByCriteria(criteria);
    }
}
