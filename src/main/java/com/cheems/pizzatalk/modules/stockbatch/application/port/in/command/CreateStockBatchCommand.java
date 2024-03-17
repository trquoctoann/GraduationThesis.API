package com.cheems.pizzatalk.modules.stockbatch.application.port.in.command;

import com.cheems.pizzatalk.common.cqrs.CommandSelfValidating;
import com.cheems.pizzatalk.entities.enumeration.RefillStatus;
import com.cheems.pizzatalk.entities.enumeration.Unit;
import java.time.Instant;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class CreateStockBatchCommand extends CommandSelfValidating<CreateStockBatchCommand> {

    @NotNull
    private Unit unit;

    @NotNull
    private Float purchasePricePerUnit;

    private RefillStatus status;

    private Long remainingQuantity;

    @NotNull
    private Long orderedQuantity;

    private Long receivedQuantity;

    @NotNull
    private Instant orderedDate;

    private Instant receivedDate;

    private Instant expirationDate;

    @NotNull
    private Long stockItemId;
}
