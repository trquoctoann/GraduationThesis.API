package com.cheems.pizzatalk.repository;

import com.cheems.pizzatalk.entities.UserEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
    @SuppressWarnings("NullableProblems")
    List<UserEntity> findAll(@Nullable Specification<UserEntity> spec);

    @SuppressWarnings("NullableProblems")
    Page<UserEntity> findAll(@Nullable Specification<UserEntity> spec, Pageable pageable);
}
