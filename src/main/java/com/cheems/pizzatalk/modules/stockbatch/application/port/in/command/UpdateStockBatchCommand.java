package com.cheems.pizzatalk.modules.stockbatch.application.port.in.command;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class UpdateStockBatchCommand extends CreateStockBatchCommand {

    @NotNull
    private Long id;
}
