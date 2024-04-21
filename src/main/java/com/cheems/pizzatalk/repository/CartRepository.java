package com.cheems.pizzatalk.repository;

import com.cheems.pizzatalk.entities.CartEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long>, JpaSpecificationExecutor<CartEntity> {
    @SuppressWarnings("NullableProblems")
    List<CartEntity> findAll(@Nullable Specification<CartEntity> spec);

    @SuppressWarnings("NullableProblems")
    Page<CartEntity> findAll(@Nullable Specification<CartEntity> spec, Pageable pageable);
}
