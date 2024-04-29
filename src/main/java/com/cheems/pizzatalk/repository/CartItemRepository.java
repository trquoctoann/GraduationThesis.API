package com.cheems.pizzatalk.repository;

import com.cheems.pizzatalk.entities.CartItemEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long>, JpaSpecificationExecutor<CartItemEntity> {
    @SuppressWarnings("NullableProblems")
    List<CartItemEntity> findAll(@Nullable Specification<CartItemEntity> spec);

    @SuppressWarnings("NullableProblems")
    Page<CartItemEntity> findAll(@Nullable Specification<CartItemEntity> spec, Pageable pageable);
}
