package com.cheems.pizzatalk.modules.cart.application.port.in.command;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class UpdateCartCommand extends CreateCartCommand {

    @NotNull
    private Long id;
}
