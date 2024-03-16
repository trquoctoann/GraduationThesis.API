package com.cheems.pizzatalk.modules.stockitem.application.port.in.command;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class UpdateStockItemCommand extends CreateStockItemCommand {

    @NotNull
    private Long id;
}
