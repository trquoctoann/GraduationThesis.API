package com.cheems.pizzatalk.modules.cartitem.application.port.in.command;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class UpdateCartItemCommand extends CreateCartItemCommand {

    @NotNull
    private Long id;
}
