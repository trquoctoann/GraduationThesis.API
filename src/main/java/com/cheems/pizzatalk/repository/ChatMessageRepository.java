package com.cheems.pizzatalk.repository;

import com.cheems.pizzatalk.entities.ChatMessageEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long>, JpaSpecificationExecutor<ChatMessageEntity> {
    @SuppressWarnings("NullableProblems")
    List<ChatMessageEntity> findAll(@Nullable Specification<ChatMessageEntity> spec);

    @SuppressWarnings("NullableProblems")
    Page<ChatMessageEntity> findAll(@Nullable Specification<ChatMessageEntity> spec, Pageable pageable);
}
