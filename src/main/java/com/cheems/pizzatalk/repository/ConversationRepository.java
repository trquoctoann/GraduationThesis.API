package com.cheems.pizzatalk.repository;

import com.cheems.pizzatalk.entities.ConversationEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends JpaRepository<ConversationEntity, Long>, JpaSpecificationExecutor<ConversationEntity> {
    @SuppressWarnings("NullableProblems")
    List<ConversationEntity> findAll(@Nullable Specification<ConversationEntity> spec);

    @SuppressWarnings("NullableProblems")
    Page<ConversationEntity> findAll(@Nullable Specification<ConversationEntity> spec, Pageable pageable);
}
