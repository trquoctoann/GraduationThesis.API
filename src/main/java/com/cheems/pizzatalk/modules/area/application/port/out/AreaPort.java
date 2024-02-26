package com.cheems.pizzatalk.modules.area.application.port.out;

import com.cheems.pizzatalk.modules.area.domain.Area;

public interface AreaPort {
    Area save(Area area);

    void deleteById(Long id);
}
