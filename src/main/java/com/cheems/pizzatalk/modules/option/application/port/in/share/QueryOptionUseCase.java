package com.cheems.pizzatalk.modules.option.application.port.in.share;

import com.cheems.pizzatalk.modules.option.application.port.in.query.OptionCriteria;
import com.cheems.pizzatalk.modules.option.domain.Option;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryOptionUseCase {
    Optional<Option> findById(Long id, String... fetchAttributes);

    Option getById(Long id, String... fetchAttributes);

    Optional<Option> findByCode(String code, String... fetchAttributes);

    Option getByCode(String code, String... fetchAttributes);

    Optional<Option> findByCriteria(OptionCriteria criteria);

    Option getByCriteria(OptionCriteria criteria);

    List<Option> findListByCriteria(OptionCriteria criteria);

    Page<Option> findPageByCriteria(OptionCriteria criteria, Pageable pageable);
}
