package com.cheems.pizzatalk.modules.area.application.port.out;

import com.cheems.pizzatalk.modules.area.application.port.in.query.AreaCriteria;
import com.cheems.pizzatalk.modules.area.domain.Area;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryAreaPort {
    Optional<Area> findByCriteria(AreaCriteria criteria);

    List<Area> findListByCriteria(AreaCriteria criteria);

    Page<Area> findPageByCriteria(AreaCriteria criteria, Pageable pageable);
}
