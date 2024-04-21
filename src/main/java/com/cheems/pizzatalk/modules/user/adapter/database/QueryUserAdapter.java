package com.cheems.pizzatalk.modules.user.adapter.database;

import com.cheems.pizzatalk.common.exception.AdapterException;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.service.QueryService;
import com.cheems.pizzatalk.common.specification.SpecificationUtils;
import com.cheems.pizzatalk.entities.*;
import com.cheems.pizzatalk.entities.enumeration.UserStatus;
import com.cheems.pizzatalk.entities.mapper.CartMapper;
import com.cheems.pizzatalk.entities.mapper.ConversationMapper;
import com.cheems.pizzatalk.entities.mapper.RoleMapper;
import com.cheems.pizzatalk.entities.mapper.UserMapper;
import com.cheems.pizzatalk.modules.user.application.port.in.query.UserCriteria;
import com.cheems.pizzatalk.modules.user.application.port.out.QueryUserPort;
import com.cheems.pizzatalk.modules.user.domain.User;
import com.cheems.pizzatalk.repository.UserRepository;
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
public class QueryUserAdapter extends QueryService<UserEntity> implements QueryUserPort {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final RoleMapper roleMapper;

    private final ConversationMapper conversationMapper;

    private final CartMapper cartMapper;

    public QueryUserAdapter(
        UserRepository userRepository,
        UserMapper userMapper,
        RoleMapper roleMapper,
        ConversationMapper conversationMapper,
        CartMapper cartMapper
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.conversationMapper = conversationMapper;
        this.cartMapper = cartMapper;
    }

    @Override
    public Optional<User> findByCriteria(UserCriteria criteria) {
        List<UserEntity> userEntities = userRepository.findAll(createSpecification(criteria));
        if (CollectionUtils.isEmpty(userEntities)) {
            return Optional.empty();
        }
        Integer userEntitiesSize = userEntities.size();
        if (userEntitiesSize > 1) {
            Set<Long> userIds = userEntities.stream().map(UserEntity::getId).collect(Collectors.toSet());
            throw new AdapterException("Found more than one user: " + userIds);
        }
        return Optional.of(userEntities.get(0)).map(userEntity -> toDomainModel(userEntity, criteria.getFetchAttributes()));
    }

    @Override
    public List<User> findListByCriteria(UserCriteria criteria) {
        return userRepository
            .findAll(createSpecification(criteria))
            .stream()
            .map(userEntity -> toDomainModel(userEntity, criteria.getFetchAttributes()))
            .collect(Collectors.toList());
    }

    @Override
    public Page<User> findPageByCriteria(UserCriteria criteria, Pageable pageable) {
        Page<UserEntity> userEntitiesPage = userRepository.findAll(createSpecification(criteria), pageable);
        return new PageImpl<>(userMapper.toDomain(userEntitiesPage.getContent()), pageable, userEntitiesPage.getTotalElements());
    }

    private Specification<UserEntity> createSpecification(UserCriteria criteria) {
        Set<String> fetchAttributes = criteria.getFetchAttributes();
        Specification<UserEntity> specification = Specification.where(null);
        specification.and((root, criteriaQuery, builder) -> builder.notEqual(root.get(UserEntity_.status), UserStatus.DELETED));

        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserEntity_.id));
            }
            if (criteria.getUsername() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUsername(), UserEntity_.username));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), UserEntity_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), UserEntity_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), UserEntity_.email));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), UserEntity_.status));
            }
            if (criteria.getLangKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLangKey(), UserEntity_.langKey));
            }
            if (criteria.getIsOnline() != null) {
                specification = specification.and(buildSpecification(criteria.getIsOnline(), UserEntity_.isOnline));
            }
            if (criteria.getLastOnlineAt() != null) {
                specification = specification.and(buildSpecification(criteria.getLastOnlineAt(), UserEntity_.lastOnlineAt));
            }
            if (criteria.getRoleId() != null) {
                specification = specification.and(buildSpecificationFindByRoleId(criteria.getRoleId()));
            }
            if (!CollectionUtils.isEmpty(fetchAttributes)) {
                specification = specification.and(SpecificationUtils.fetchAttributes(userMapper.toEntityAttributes(fetchAttributes)));
            }
            specification = specification.and(SpecificationUtils.distinct(true));
        }
        return specification;
    }

    private Specification<UserEntity> buildSpecificationFindByRoleId(RangeFilter<Long> roleId) {
        if (roleId.getEquals() != null) {
            return (root, query, builder) -> {
                Join<UserEntity, UserRoleEntity> joinUserRole = SpecificationUtils.getJoinFetch(root, "userRoles", JoinType.LEFT, false);
                Join<UserRoleEntity, RoleEntity> joinRole = SpecificationUtils.getJoinFetch(joinUserRole, "role", JoinType.LEFT, false);
                return builder.equal(joinRole.get(RoleEntity_.id), roleId.getEquals());
            };
        }
        if (roleId.getIn() != null) {
            return (root, query, builder) -> {
                Join<UserEntity, UserRoleEntity> joinUserRole = SpecificationUtils.getJoinFetch(root, "userRoles", JoinType.LEFT, false);
                Join<UserRoleEntity, RoleEntity> joinRole = SpecificationUtils.getJoinFetch(joinUserRole, "role", JoinType.LEFT, false);
                return joinRole.get(RoleEntity_.id).in(roleId.getIn());
            };
        }
        return null;
    }

    private User toDomainModel(UserEntity userEntity, Set<String> domainAttributes) {
        User user = userMapper.toDomain(userEntity);
        return enrichUserDomain(user, userEntity, domainAttributes);
    }

    private User enrichUserDomain(User user, UserEntity userEntity, Set<String> domainAttributes) {
        if (CollectionUtils.isEmpty(domainAttributes)) {
            return user;
        }

        if (domainAttributes.contains(UserMapper.DOMAIN_ROLE)) {
            user.setRoles(
                userEntity
                    .getUserRoles()
                    .stream()
                    .map(userRoleEntity -> roleMapper.toDomain(userRoleEntity.getRole()))
                    .collect(Collectors.toSet())
            );
        }
        if (domainAttributes.contains(UserMapper.DOMAIN_PERMISSION)) {
            user.setPermissions(
                userEntity
                    .getUserRoles()
                    .stream()
                    .flatMap(userRoleEntity -> userRoleEntity.getRole().getRolePermissions().stream())
                    .map(rolePermissionEntity -> rolePermissionEntity.getPermission().getName())
                    .collect(Collectors.toSet())
            );
        }
        if (domainAttributes.contains(UserMapper.DOMAIN_CONVERSATION)) {
            user.setConversations(
                userEntity
                    .getParticipants()
                    .stream()
                    .map(participantEntity -> conversationMapper.toDomain(participantEntity.getConversation()))
                    .collect(Collectors.toSet())
            );
        }
        if (domainAttributes.contains(UserMapper.DOMAIN_CART)) {
            user.setCarts(userEntity.getCarts().stream().map(cartEntity -> cartMapper.toDomain(cartEntity)).collect(Collectors.toSet()));
        }
        return user;
    }
}
