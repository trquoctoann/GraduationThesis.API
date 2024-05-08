package com.cheems.pizzatalk.repository;

import com.cheems.pizzatalk.entities.AttachmentEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<AttachmentEntity, Long>, JpaSpecificationExecutor<AttachmentEntity> {
    @SuppressWarnings("NullableProblems")
    List<AttachmentEntity> findAll(@Nullable Specification<AttachmentEntity> spec);

    @SuppressWarnings("NullableProblems")
    Page<AttachmentEntity> findAll(@Nullable Specification<AttachmentEntity> spec, Pageable pageable);
}
