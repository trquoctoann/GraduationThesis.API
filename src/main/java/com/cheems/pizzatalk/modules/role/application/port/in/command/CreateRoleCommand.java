package com.cheems.pizzatalk.modules.role.application.port.in.command;

import com.cheems.pizzatalk.common.cqrs.CommandSelfValidating;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class CreateRoleCommand extends CommandSelfValidating<CreateRoleCommand> {

    @NotNull
    private String name;
}
