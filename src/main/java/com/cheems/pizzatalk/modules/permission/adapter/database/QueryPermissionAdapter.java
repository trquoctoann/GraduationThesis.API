package com.cheems.pizzatalk.modules.permission.adapter.database;

import com.cheems.pizzatalk.common.exception.AdapterException;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.service.QueryService;
import com.cheems.pizzatalk.common.specification.SpecificationUtils;
import com.cheems.pizzatalk.entities.PermissionEntity;
import com.cheems.pizzatalk.entities.PermissionEntity_;
import com.cheems.pizzatalk.entities.RolePermissionEntity;
import com.cheems.pizzatalk.entities.RolePermissionEntity_;
import com.cheems.pizzatalk.entities.mapper.PermissionMapper;
import com.cheems.pizzatalk.modules.permission.application.port.in.query.PermissionCriteria;
import com.cheems.pizzatalk.modules.permission.application.port.out.QueryPermissionPort;
import com.cheems.pizzatalk.modules.permission.domain.Permission;
import com.cheems.pizzatalk.repository.PermissionRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class QueryPermissionAdapter extends QueryService<PermissionEntity> implements QueryPermissionPort {

    private final PermissionRepository permissionRepository;

    private final PermissionMapper permissionMapper;

    public QueryPermissionAdapter(PermissionRepository permissionRepository, PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public Optional<Permission> findByCriteria(PermissionCriteria criteria) {
        List<PermissionEntity> permissionEntities = permissionRepository.findAll(createSpecification(criteria));
        if (CollectionUtils.isEmpty(permissionEntities)) {
            return Optional.empty();
        }

        Integer permissionEntitiesSize = permissionEntities.size();
        if (permissionEntitiesSize > 1) {
            Set<Long> permissionIds = permissionEntities.stream().map(PermissionEntity::getId).collect(Collectors.toSet());
            throw new AdapterException("Found more than one permission: " + permissionIds);
        }
        return Optional
            .of(permissionEntities.get(0))
            .map(permissionEntity -> toDomainModel(permissionEntity, criteria.getFetchAttributes()));
    }

    @Override
    public List<Permission> findListByCriteria(PermissionCriteria criteria) {
        return permissionRepository
            .findAll(createSpecification(criteria))
            .stream()
            .map(permissionEntity -> toDomainModel(permissionEntity, criteria.getFetchAttributes()))
            .collect(Collectors.toList());
    }

    @Override
    public Page<Permission> findPageByCriteria(PermissionCriteria criteria, Pageable pageable) {
        Page<PermissionEntity> permissionEntitiesPage = permissionRepository.findAll(createSpecification(criteria), pageable);
        return new PageImpl<>(
            permissionMapper.toDomain(permissionEntitiesPage.getContent()),
            pageable,
            permissionEntitiesPage.getTotalElements()
        );
    }

    private Specification<PermissionEntity> createSpecification(PermissionCriteria criteria) {
        Set<String> fetchAttributes = criteria.getFetchAttributes();
        Specification<PermissionEntity> specification = Specification.where(null);

        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PermissionEntity_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), PermissionEntity_.name));
            }
            if (criteria.getRoleId() != null) {
                specification = specification.and(buildSpecificationFindByRoleId(criteria.getRoleId()));
            }
            if (!CollectionUtils.isEmpty(fetchAttributes)) {
                specification = specification.and(SpecificationUtils.fetchAttributes(permissionMapper.toEntityAttributes(fetchAttributes)));
            }
            specification = specification.and(SpecificationUtils.distinct(true));
        }
        return specification;
    }

    private Specification<PermissionEntity> buildSpecificationFindByRoleId(RangeFilter<Long> roleId) {
        if (roleId.getEquals() != null) {
            return (root, query, builder) -> {
                Join<PermissionEntity, RolePermissionEntity> joinRolePermission = SpecificationUtils.getJoinFetch(
                    root,
                    "rolePermissions",
                    JoinType.LEFT,
                    false
                );
                return builder.equal(joinRolePermission.get(RolePermissionEntity_.role), roleId.getEquals());
            };
        }
        return null;
    }

    private Permission toDomainModel(PermissionEntity permissionEntity, Set<String> domainAttributes) {
        Permission permission = permissionMapper.toDomain(permissionEntity);
        return enrichPermissionDomain(permission, permissionEntity, domainAttributes);
    }

    private Permission enrichPermissionDomain(Permission permission, PermissionEntity permissionEntity, Set<String> domainAttributes) {
        if (CollectionUtils.isEmpty(domainAttributes)) {
            return permission;
        }
        return permission;
    }
}
