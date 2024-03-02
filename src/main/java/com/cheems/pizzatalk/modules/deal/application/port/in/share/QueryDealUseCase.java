package com.cheems.pizzatalk.modules.deal.application.port.in.share;

import com.cheems.pizzatalk.modules.deal.application.port.in.query.DealCriteria;
import com.cheems.pizzatalk.modules.deal.domain.Deal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryDealUseCase {
    Optional<Deal> findById(Long id, String... fetchAttributes);

    Deal getById(Long id, String... fetchAttributes);

    Optional<Deal> findBySlug(String slug, String... fetchAttributes);

    Deal getBySlug(String slug, String... fetchAttributes);

    Optional<Deal> findByCriteria(DealCriteria criteria);

    Deal getByCriteria(DealCriteria criteria);

    List<Deal> findListByCriteria(DealCriteria criteria);

    Page<Deal> findPageByCriteria(DealCriteria criteria, Pageable pageable);
}
