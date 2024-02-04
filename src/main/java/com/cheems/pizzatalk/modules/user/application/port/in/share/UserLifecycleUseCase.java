package com.cheems.pizzatalk.modules.user.application.port.in.share;

import com.cheems.pizzatalk.modules.user.application.port.in.command.CreateUserCommand;
import com.cheems.pizzatalk.modules.user.application.port.in.command.UpdateUserCommand;
import com.cheems.pizzatalk.modules.user.domain.User;

public interface UserLifecycleUseCase {
    User create(CreateUserCommand command);

    User update(UpdateUserCommand command);

    void deleteById(Long id);

    void deleteByUsername(String username);
}
