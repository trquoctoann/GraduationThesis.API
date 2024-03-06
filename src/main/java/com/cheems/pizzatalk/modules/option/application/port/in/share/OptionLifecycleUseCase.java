package com.cheems.pizzatalk.modules.option.application.port.in.share;

import com.cheems.pizzatalk.modules.option.application.port.in.command.CreateOptionCommand;
import com.cheems.pizzatalk.modules.option.application.port.in.command.UpdateOptionCommand;
import com.cheems.pizzatalk.modules.option.domain.Option;

public interface OptionLifecycleUseCase {
    Option create(CreateOptionCommand command);

    Option update(UpdateOptionCommand command);

    void deleteById(Long id);
}
