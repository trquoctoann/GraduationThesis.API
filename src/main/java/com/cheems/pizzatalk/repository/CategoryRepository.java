package com.cheems.pizzatalk.repository;

import com.cheems.pizzatalk.entities.CategoryEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long>, JpaSpecificationExecutor<CategoryEntity> {
    @SuppressWarnings("NullableProblems")
    List<CategoryEntity> findAll(@Nullable Specification<CategoryEntity> spec);

    @SuppressWarnings("NullableProblems")
    Page<CategoryEntity> findAll(@Nullable Specification<CategoryEntity> spec, Pageable pageable);
}
