package com.cheems.pizzatalk.modules.optiondetail.application.port.in.share;

import com.cheems.pizzatalk.entities.enumeration.CommerceStatus;
import com.cheems.pizzatalk.modules.optiondetail.application.port.in.command.CreateOptionDetailCommand;
import com.cheems.pizzatalk.modules.optiondetail.application.port.in.command.UpdateOptionDetailCommand;
import com.cheems.pizzatalk.modules.optiondetail.domain.OptionDetail;

public interface OptionDetailLifecycleUseCase {
    OptionDetail create(CreateOptionDetailCommand command);

    OptionDetail update(UpdateOptionDetailCommand command);

    OptionDetail updateCommerceStatus(Long id, CommerceStatus newStatus);

    void deleteById(Long id);
}
