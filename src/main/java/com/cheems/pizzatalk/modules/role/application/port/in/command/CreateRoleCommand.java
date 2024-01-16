package com.cheems.pizzatalk.modules.role.application.port.in.command;

import com.cheems.pizzatalk.common.cqrs.CommandSelfValidating;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class CreateRoleCommand extends CommandSelfValidating<CreateRoleCommand> {

    private String authority;
}
