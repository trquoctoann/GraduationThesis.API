package com.cheems.pizzatalk.repository;

import com.cheems.pizzatalk.entities.DealEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface DealRepository extends JpaRepository<DealEntity, Long>, JpaSpecificationExecutor<DealEntity> {
    @SuppressWarnings("NullableProblems")
    List<DealEntity> findAll(@Nullable Specification<DealEntity> spec);

    @SuppressWarnings("NullableProblems")
    Page<DealEntity> findAll(@Nullable Specification<DealEntity> spec, Pageable pageable);
}
