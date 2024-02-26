package com.cheems.pizzatalk.modules.area.application.port.in.share;

import com.cheems.pizzatalk.modules.area.application.port.in.query.AreaCriteria;
import com.cheems.pizzatalk.modules.area.domain.Area;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryAreaUseCase {
    Optional<Area> findById(Long id, String... fetchAttributes);

    Area getById(Long id, String... fetchAttributes);

    Optional<Area> findByCode(String code, String... fetchAttributes);

    Area getByCode(String code, String... fetchAttributes);

    Optional<Area> findByCriteria(AreaCriteria criteria);

    Area getByCriteria(AreaCriteria criteria);

    List<Area> findListByCriteria(AreaCriteria criteria);

    Page<Area> findPageByCriteria(AreaCriteria criteria, Pageable pageable);
}
