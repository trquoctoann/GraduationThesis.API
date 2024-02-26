package com.cheems.pizzatalk.repository;

import com.cheems.pizzatalk.entities.AreaEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaRepository extends JpaRepository<AreaEntity, Long>, JpaSpecificationExecutor<AreaEntity> {
    @SuppressWarnings("NullableProblems")
    List<AreaEntity> findAll(@Nullable Specification<AreaEntity> spec);

    @SuppressWarnings("NullableProblems")
    Page<AreaEntity> findAll(@Nullable Specification<AreaEntity> spec, Pageable pageable);
}
