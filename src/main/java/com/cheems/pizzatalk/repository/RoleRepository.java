package com.cheems.pizzatalk.repository;

import com.cheems.pizzatalk.entities.RoleEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long>, JpaSpecificationExecutor<RoleEntity> {
    @SuppressWarnings("NullableProblems")
    List<RoleEntity> findAll(@Nullable Specification<RoleEntity> spec);

    @SuppressWarnings("NullableProblems")
    Page<RoleEntity> findAll(@Nullable Specification<RoleEntity> spec, Pageable pageable);
}
