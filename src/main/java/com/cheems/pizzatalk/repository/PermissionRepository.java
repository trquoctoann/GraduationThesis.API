package com.cheems.pizzatalk.repository;

import com.cheems.pizzatalk.entities.PermissionEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long>, JpaSpecificationExecutor<PermissionEntity> {
    @SuppressWarnings("NullableProblems")
    List<PermissionEntity> findAll(@Nullable Specification<PermissionEntity> spec);

    @SuppressWarnings("NullableProblems")
    Page<PermissionEntity> findAll(@Nullable Specification<PermissionEntity> spec, Pageable pageable);
}
