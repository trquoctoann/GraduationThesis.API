package com.cheems.pizzatalk.modules.user.application.port.in.share;

import com.cheems.pizzatalk.modules.user.application.port.in.command.CreateUserCommand;
import com.cheems.pizzatalk.modules.user.application.port.in.command.UpdateUserCommand;
import com.cheems.pizzatalk.modules.user.application.port.in.command.UpdateUserPasswordCommand;
import com.cheems.pizzatalk.modules.user.domain.User;
import java.util.Optional;

public interface UserLifecycleUseCase {
    User create(CreateUserCommand command);

    User update(UpdateUserCommand command);

    void deleteById(Long id);

    void deleteByUsername(String username);

    Optional<User> activateUser(String activationKey);

    Optional<User> requestResetPassword(String email);

    Optional<User> resetPassword(UpdateUserPasswordCommand command);
}
