package com.cheems.pizzatalk.modules.user.application.port.in.command;

import com.cheems.pizzatalk.common.cqrs.CommandSelfValidating;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResetPasswordUserPasswordCommand extends CommandSelfValidating<ResetPasswordUserPasswordCommand> {

    @NotNull
    private String key;

    @NotNull
    private String newPassword;
}
