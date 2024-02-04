package com.cheems.pizzatalk.modules.user.application.port.in.command;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class UpdateUserCommand extends CreateUserCommand {

    @NotNull
    private Long id;
}
