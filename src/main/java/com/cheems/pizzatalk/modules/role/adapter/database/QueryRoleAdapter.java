package com.cheems.pizzatalk.modules.role.adapter.database;

import com.cheems.pizzatalk.common.exception.AdapterException;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.service.QueryService;
import com.cheems.pizzatalk.common.specification.SpecificationUtils;
import com.cheems.pizzatalk.entities.RoleEntity;
import com.cheems.pizzatalk.entities.RoleEntity_;
import com.cheems.pizzatalk.entities.UserRoleEntity;
import com.cheems.pizzatalk.entities.UserRoleEntity_;
import com.cheems.pizzatalk.entities.mapper.RoleMapper;
import com.cheems.pizzatalk.modules.role.application.port.in.query.RoleCriteria;
import com.cheems.pizzatalk.modules.role.application.port.out.QueryRolePort;
import com.cheems.pizzatalk.modules.role.domain.Role;
import com.cheems.pizzatalk.repository.RoleRepository;
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
public class QueryRoleAdapter extends QueryService<RoleEntity> implements QueryRolePort {

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    public QueryRoleAdapter(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public Optional<Role> findByCriteria(RoleCriteria criteria) {
        List<RoleEntity> roleEntities = roleRepository.findAll(createSpecification(criteria));
        if (CollectionUtils.isEmpty(roleEntities)) {
            return Optional.empty();
        }
        Integer roleEntitiesSize = roleEntities.size();
        if (roleEntitiesSize > 1) {
            Set<Long> roleIds = roleEntities.stream().map(RoleEntity::getId).collect(Collectors.toSet());
            throw new AdapterException("Found more than one role: " + roleIds);
        }
        return Optional.of(roleEntities.get(0)).map(roleEntity -> toDomainModel(roleEntity, criteria.getFetchAttributes()));
    }

    @Override
    public List<Role> findListByCriteria(RoleCriteria criteria) {
        return roleRepository
            .findAll(createSpecification(criteria))
            .stream()
            .map(roleEntity -> toDomainModel(roleEntity, criteria.getFetchAttributes()))
            .collect(Collectors.toList());
    }

    @Override
    public Page<Role> findPageByCriteria(RoleCriteria criteria, Pageable pageable) {
        Page<RoleEntity> roleEntitiesPage = roleRepository.findAll(createSpecification(criteria), pageable);
        return new PageImpl<>(roleMapper.toDomain(roleEntitiesPage.getContent()), pageable, roleEntitiesPage.getTotalElements());
    }

    private Specification<RoleEntity> createSpecification(RoleCriteria criteria) {
        Set<String> domainAttributes = criteria.getFetchAttributes();
        Specification<RoleEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RoleEntity_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), RoleEntity_.name));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecificationFindByUserId(criteria.getUserId()));
            }
            if (!CollectionUtils.isEmpty(domainAttributes)) {
                specification = specification.and(SpecificationUtils.fetchAttributes(roleMapper.toEntityAttributes(domainAttributes)));
            }
            specification = specification.and(SpecificationUtils.distinct(true));
        }
        return specification;
    }

    private Specification<RoleEntity> buildSpecificationFindByUserId(RangeFilter<Long> userId) {
        if (userId.getEquals() != null) {
            return (root, query, builder) -> {
                Join<RoleEntity, UserRoleEntity> joinUserRoles = SpecificationUtils.getJoinFetch(root, "userRoles", JoinType.LEFT, false);
                return builder.equal(joinUserRoles.get(UserRoleEntity_.user), userId.getEquals());
            };
        }
        return null;
    }

    private Role toDomainModel(RoleEntity roleEntity, Set<String> domainAttributes) {
        Role role = roleMapper.toDomain(roleEntity);
        return enrichRoleDomain(role, roleEntity, domainAttributes);
    }

    private Role enrichRoleDomain(Role role, RoleEntity roleEntity, Set<String> domainAttributes) {
        if (CollectionUtils.isEmpty(domainAttributes)) {
            return role;
        }

        if (domainAttributes.contains(RoleMapper.DOMAIN_PERMISSION)) {
            role.setPermissions(
                roleEntity
                    .getRolePermissions()
                    .stream()
                    .map(rolePermissionEntity -> rolePermissionEntity.getPermission().getName())
                    .collect(Collectors.toSet())
            );
        }
        return role;
    }
}
