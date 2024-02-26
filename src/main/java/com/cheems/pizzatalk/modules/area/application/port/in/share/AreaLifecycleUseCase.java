package com.cheems.pizzatalk.modules.area.application.port.in.share;

import com.cheems.pizzatalk.modules.area.application.port.in.command.CreateAreaCommand;
import com.cheems.pizzatalk.modules.area.application.port.in.command.UpdateAreaCommand;
import com.cheems.pizzatalk.modules.area.domain.Area;

public interface AreaLifecycleUseCase {
    Area create(CreateAreaCommand command);

    Area update(UpdateAreaCommand command);

    void deleteById(Long id);
}
