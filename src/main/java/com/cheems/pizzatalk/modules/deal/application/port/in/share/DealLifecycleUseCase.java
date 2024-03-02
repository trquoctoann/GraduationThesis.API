package com.cheems.pizzatalk.modules.deal.application.port.in.share;

import com.cheems.pizzatalk.modules.deal.application.port.in.command.CreateDealCommand;
import com.cheems.pizzatalk.modules.deal.application.port.in.command.UpdateDealCommand;
import com.cheems.pizzatalk.modules.deal.domain.Deal;

public interface DealLifecycleUseCase {
    Deal create(CreateDealCommand command);

    Deal update(UpdateDealCommand command);

    void deleteById(Long id);
}
