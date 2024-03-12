package com.cheems.pizzatalk.modules.userkey.adapter.database;

import com.cheems.pizzatalk.common.exception.AdapterException;
import com.cheems.pizzatalk.common.service.QueryService;
import com.cheems.pizzatalk.common.specification.SpecificationUtils;
import com.cheems.pizzatalk.entities.UserEntity_;
import com.cheems.pizzatalk.entities.UserKeyEntity;
import com.cheems.pizzatalk.entities.UserKeyEntity_;
import com.cheems.pizzatalk.entities.mapper.UserKeyMapper;
import com.cheems.pizzatalk.entities.mapper.UserMapper;
import com.cheems.pizzatalk.modules.userkey.application.port.in.query.UserKeyCriteria;
import com.cheems.pizzatalk.modules.userkey.application.port.out.QueryUserKeyPort;
import com.cheems.pizzatalk.modules.userkey.domain.UserKey;
import com.cheems.pizzatalk.repository.UserKeyRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.criteria.JoinType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class QueryUserKeyAdapter extends QueryService<UserKeyEntity> implements QueryUserKeyPort {

    private final UserKeyRepository userKeyRepository;

    private final UserKeyMapper userKeyMapper;

    private final UserMapper userMapper;

    public QueryUserKeyAdapter(UserKeyRepository userKeyRepository, UserKeyMapper userKeyMapper, UserMapper userMapper) {
        this.userKeyRepository = userKeyRepository;
        this.userKeyMapper = userKeyMapper;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<UserKey> findByCriteria(UserKeyCriteria criteria) {
        List<UserKeyEntity> userKeyEntities = userKeyRepository.findAll(createSpecification(criteria));
        if (CollectionUtils.isEmpty(userKeyEntities)) {
            return Optional.empty();
        }
        Integer userKeyEntitiesSize = userKeyEntities.size();
        if (userKeyEntitiesSize > 1) {
            Set<Long> userKeyIds = userKeyEntities.stream().map(UserKeyEntity::getId).collect(Collectors.toSet());
            throw new AdapterException("Found more than one user key: " + userKeyIds);
        }
        return Optional.of(userKeyEntities.get(0)).map(userKeyEntity -> toDomainModel(userKeyEntity, criteria.getFetchAttributes()));
    }

    @Override
    public List<UserKey> findListByCriteria(UserKeyCriteria criteria) {
        return userKeyRepository
            .findAll(createSpecification(criteria))
            .stream()
            .map(userKeyEntity -> toDomainModel(userKeyEntity, criteria.getFetchAttributes()))
            .collect(Collectors.toList());
    }

    @Override
    public Page<UserKey> findPageByCriteria(UserKeyCriteria criteria, Pageable pageable) {
        Page<UserKeyEntity> userKeyEntitiesPage = userKeyRepository.findAll(createSpecification(criteria), pageable);
        return new PageImpl<>(userKeyMapper.toDomain(userKeyEntitiesPage.getContent()), pageable, userKeyEntitiesPage.getTotalElements());
    }

    private Specification<UserKeyEntity> createSpecification(UserKeyCriteria criteria) {
        Set<String> fetchAttributes = criteria.getFetchAttributes();
        Specification<UserKeyEntity> specification = Specification.where(null);

        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserKeyEntity_.id));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), UserKeyEntity_.value));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), UserKeyEntity_.type));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), UserKeyEntity_.status));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildSpecification(criteria.getCreationDate(), UserKeyEntity_.creationDate));
            }
            if (criteria.getUsedDate() != null) {
                specification = specification.and(buildSpecification(criteria.getUsedDate(), UserKeyEntity_.usedDate));
            }
            if (criteria.getExpirationDate() != null) {
                specification = specification.and(buildSpecification(criteria.getExpirationDate(), UserKeyEntity_.expirationDate));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(UserKeyEntity_.user, JoinType.LEFT).get(UserEntity_.id))
                    );
            }
            if (!CollectionUtils.isEmpty(fetchAttributes)) {
                specification = specification.and(SpecificationUtils.fetchAttributes(userKeyMapper.toEntityAttributes(fetchAttributes)));
            }
            specification = specification.and(SpecificationUtils.distinct(true));
        }
        return specification;
    }

    private UserKey toDomainModel(UserKeyEntity userKeyEntity, Set<String> domainAttributes) {
        UserKey userKey = userKeyMapper.toDomain(userKeyEntity);
        return enrichUserKeyDomain(userKey, userKeyEntity, domainAttributes);
    }

    private UserKey enrichUserKeyDomain(UserKey userKey, UserKeyEntity userKeyEntity, Set<String> domainAttributes) {
        if (CollectionUtils.isEmpty(domainAttributes)) {
            return userKey;
        }

        if (domainAttributes.contains(UserKeyMapper.DOMAIN_USER)) {
            userKey.setUser(userMapper.toDomain(userKeyEntity.getUser()));
        }
        return userKey;
    }
}
