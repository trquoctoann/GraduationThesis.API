package com.cheems.pizzatalk.repository;

import com.cheems.pizzatalk.entities.UserKeyEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface UserKeyRepository extends JpaRepository<UserKeyEntity, Long>, JpaSpecificationExecutor<UserKeyEntity> {
    @SuppressWarnings("NullableProblems")
    List<UserKeyEntity> findAll(@Nullable Specification<UserKeyEntity> spec);

    @SuppressWarnings("NullableProblems")
    Page<UserKeyEntity> findAll(@Nullable Specification<UserKeyEntity> spec, Pageable pageable);
}
