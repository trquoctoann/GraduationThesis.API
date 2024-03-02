package com.cheems.pizzatalk.modules.deal.application.port.out;

import com.cheems.pizzatalk.modules.deal.application.port.in.query.DealCriteria;
import com.cheems.pizzatalk.modules.deal.domain.Deal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryDealPort {
    Optional<Deal> findByCriteria(DealCriteria criteria);

    List<Deal> findListByCriteria(DealCriteria criteria);

    Page<Deal> findPageByCriteria(DealCriteria criteria, Pageable pageable);
}
