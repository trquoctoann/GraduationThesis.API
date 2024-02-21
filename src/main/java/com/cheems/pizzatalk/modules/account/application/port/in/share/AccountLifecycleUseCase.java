package com.cheems.pizzatalk.modules.account.application.port.in.share;

import com.cheems.pizzatalk.modules.user.application.port.in.command.ResetPasswordUserPasswordCommand;
import com.cheems.pizzatalk.modules.user.domain.User;
import java.util.Optional;

public interface AccountLifecycleUseCase {
    Optional<User> activateAccount(String activationKey);

    Optional<User> requestResetPassword(String email);

    Optional<User> resetPassword(ResetPasswordUserPasswordCommand command);
}
