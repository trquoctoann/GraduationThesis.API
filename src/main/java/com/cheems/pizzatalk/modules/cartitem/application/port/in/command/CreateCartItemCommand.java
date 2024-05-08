package com.cheems.pizzatalk.modules.cartitem.application.port.in.command;

import com.cheems.pizzatalk.common.cqrs.CommandSelfValidating;
import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class CreateCartItemCommand extends CommandSelfValidating<CreateCartItemCommand> {

    @NotNull
    private Integer quantity;

    @NotNull
    private Long cartId;

    @NotNull
    private Long productId;

    private Set<Long> optionDetailIds;
}
