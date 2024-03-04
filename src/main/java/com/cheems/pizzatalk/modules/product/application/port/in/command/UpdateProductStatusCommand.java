package com.cheems.pizzatalk.modules.product.application.port.in.command;

import com.cheems.pizzatalk.common.cqrs.CommandSelfValidating;
import com.cheems.pizzatalk.entities.enumeration.CommerceStatus;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class UpdateProductStatusCommand extends CommandSelfValidating<UpdateProductStatusCommand> {

    @NotNull
    private Long id;

    @NotNull
    private CommerceStatus status;
}
