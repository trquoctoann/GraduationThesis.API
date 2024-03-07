package com.cheems.pizzatalk.repository;

import com.cheems.pizzatalk.entities.OptionDetailEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionDetailRepository extends JpaRepository<OptionDetailEntity, Long>, JpaSpecificationExecutor<OptionDetailEntity> {
    @SuppressWarnings("NullableProblems")
    List<OptionDetailEntity> findAll(@Nullable Specification<OptionDetailEntity> spec);

    @SuppressWarnings("NullableProblems")
    Page<OptionDetailEntity> findAll(@Nullable Specification<OptionDetailEntity> spec, Pageable pageable);
}
