package com.cheems.pizzatalk.modules.account.application.port.in.share;

import com.cheems.pizzatalk.modules.account.application.port.in.command.AccountResetPasswordCommand;
import com.cheems.pizzatalk.modules.user.domain.User;

public interface AccountLifecycleUseCase {
    User activateAccount(String activationKey);

    User requestResetPassword(String email);

    User resetPassword(String resetKey, AccountResetPasswordCommand command);
}
