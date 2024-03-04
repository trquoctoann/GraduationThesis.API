package com.cheems.pizzatalk.repository;

import com.cheems.pizzatalk.entities.ProductEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {
    @SuppressWarnings("NullableProblems")
    List<ProductEntity> findAll(@Nullable Specification<ProductEntity> spec);

    @SuppressWarnings("NullableProblems")
    Page<ProductEntity> findAll(@Nullable Specification<ProductEntity> spec, Pageable pageable);
}
