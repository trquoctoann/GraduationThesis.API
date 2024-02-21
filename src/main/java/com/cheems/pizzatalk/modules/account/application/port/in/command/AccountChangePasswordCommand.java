package com.cheems.pizzatalk.modules.account.application.port.in.command;

import com.cheems.pizzatalk.common.cqrs.CommandSelfValidating;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class AccountChangePasswordCommand extends CommandSelfValidating<AccountChangePasswordCommand> {

    @NotNull
    private String oldPassword;

    @NotNull
    private String newPassword;
}
