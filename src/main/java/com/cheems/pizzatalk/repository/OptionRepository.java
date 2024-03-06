package com.cheems.pizzatalk.repository;

import com.cheems.pizzatalk.entities.OptionEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<OptionEntity, Long>, JpaSpecificationExecutor<OptionEntity> {
    @SuppressWarnings("NullableProblems")
    List<OptionEntity> findAll(@Nullable Specification<OptionEntity> spec);

    @SuppressWarnings("NullableProblems")
    Page<OptionEntity> findAll(@Nullable Specification<OptionEntity> spec, Pageable pageable);
}
