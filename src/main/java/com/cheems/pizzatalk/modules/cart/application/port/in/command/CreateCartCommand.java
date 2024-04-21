package com.cheems.pizzatalk.modules.cart.application.port.in.command;

import com.cheems.pizzatalk.common.cqrs.CommandSelfValidating;
import com.cheems.pizzatalk.entities.enumeration.CartStatus;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class CreateCartCommand extends CommandSelfValidating<CreateCartCommand> {

    @NotNull
    private CartStatus status;

    @NotNull
    private Long userId;
}
