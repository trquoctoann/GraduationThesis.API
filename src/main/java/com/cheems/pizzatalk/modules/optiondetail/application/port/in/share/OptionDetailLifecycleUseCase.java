package com.cheems.pizzatalk.modules.optiondetail.application.port.in.share;

import com.cheems.pizzatalk.modules.optiondetail.application.port.in.command.CreateOptionDetailCommand;
import com.cheems.pizzatalk.modules.optiondetail.application.port.in.command.UpdateOptionDetailCommand;
import com.cheems.pizzatalk.modules.optiondetail.domain.OptionDetail;

public interface OptionDetailLifecycleUseCase {
    OptionDetail create(CreateOptionDetailCommand command);

    OptionDetail update(UpdateOptionDetailCommand command);

    void deleteById(Long id);
}
