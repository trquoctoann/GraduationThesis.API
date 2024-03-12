package com.cheems.pizzatalk.modules.account.application.port.in.command;

import com.cheems.pizzatalk.common.cqrs.CommandSelfValidating;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AccountResetPasswordCommand extends CommandSelfValidating<AccountResetPasswordCommand> {

    @NotNull
    private String newPassword;
}
