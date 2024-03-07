package com.cheems.pizzatalk.modules.optiondetail.application.port.in.share;

import com.cheems.pizzatalk.modules.optiondetail.application.port.in.query.OptionDetailCriteria;
import com.cheems.pizzatalk.modules.optiondetail.domain.OptionDetail;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryOptionDetailUseCase {
    Optional<OptionDetail> findById(Long id, String... fetchAttributes);

    OptionDetail getById(Long id, String... fetchAttributes);

    Optional<OptionDetail> findBySku(String sku, String... fetchAttributes);

    OptionDetail getBySku(String sku, String... fetchAttributes);

    Optional<OptionDetail> findByCriteria(OptionDetailCriteria criteria);

    OptionDetail getByCriteria(OptionDetailCriteria criteria);

    List<OptionDetail> findListByCriteria(OptionDetailCriteria criteria);

    Page<OptionDetail> findPageByCriteria(OptionDetailCriteria criteria, Pageable pageable);
}
