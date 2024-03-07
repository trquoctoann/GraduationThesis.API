package com.cheems.pizzatalk.modules.optiondetail.application.port.out;

import com.cheems.pizzatalk.modules.optiondetail.application.port.in.query.OptionDetailCriteria;
import com.cheems.pizzatalk.modules.optiondetail.domain.OptionDetail;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryOptionDetailPort {
    Optional<OptionDetail> findByCriteria(OptionDetailCriteria criteria);

    List<OptionDetail> findListByCriteria(OptionDetailCriteria criteria);

    Page<OptionDetail> findPageByCriteria(OptionDetailCriteria criteria, Pageable pageable);
}
