package com.cheems.pizzatalk.modules.option.application.port.out;

import com.cheems.pizzatalk.modules.option.application.port.in.query.OptionCriteria;
import com.cheems.pizzatalk.modules.option.domain.Option;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryOptionPort {
    Optional<Option> findByCriteria(OptionCriteria criteria);

    List<Option> findListByCriteria(OptionCriteria criteria);

    Page<Option> findPageByCriteria(OptionCriteria criteria, Pageable pageable);
}
