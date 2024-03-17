package com.cheems.pizzatalk.repository;

import com.cheems.pizzatalk.entities.StockBatchEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface StockBatchRepository extends JpaRepository<StockBatchEntity, Long>, JpaSpecificationExecutor<StockBatchEntity> {
    @SuppressWarnings("NullableProblems")
    List<StockBatchEntity> findAll(@Nullable Specification<StockBatchEntity> spec);

    @SuppressWarnings("NullableProblems")
    Page<StockBatchEntity> findAll(@Nullable Specification<StockBatchEntity> spec, Pageable pageable);
}
