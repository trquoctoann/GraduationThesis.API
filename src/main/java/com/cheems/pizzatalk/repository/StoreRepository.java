package com.cheems.pizzatalk.repository;

import com.cheems.pizzatalk.entities.StoreEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long>, JpaSpecificationExecutor<StoreEntity> {
    @SuppressWarnings("NullableProblems")
    List<StoreEntity> findAll(@Nullable Specification<StoreEntity> spec);

    @SuppressWarnings("NullableProblems")
    Page<StoreEntity> findAll(@Nullable Specification<StoreEntity> spec, Pageable pageable);
}
