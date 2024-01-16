package com.cheems.pizzatalk.modules.role.application.port.in.command;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class UpdateRoleCommand extends CreateRoleCommand {

    @NotNull
    private Long id;
}
