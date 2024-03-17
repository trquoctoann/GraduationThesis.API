package com.cheems.pizzatalk.modules.stockbatch.domain;

import com.cheems.pizzatalk.entities.enumeration.RefillStatus;
import com.cheems.pizzatalk.entities.enumeration.Unit;
import com.cheems.pizzatalk.modules.stockitem.domain.StockItem;
import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class StockBatch implements Serializable {

    private Long id;

    @NotNull
    private Unit unit;

    @NotNull
    private Float purchasePricePerUnit;

    @NotNull
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

    private StockItem stockItem;
}
