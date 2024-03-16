package com.cheems.pizzatalk.modules.stockitem.application.port.in.command;

import com.cheems.pizzatalk.common.cqrs.CommandSelfValidating;
import com.cheems.pizzatalk.entities.enumeration.Unit;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class CreateStockItemCommand extends CommandSelfValidating<CreateStockItemCommand> {

    private String name;

    private String sku;

    @NotNull
    private Unit unit;

    private Long totalQuantity;

    @NotNull
    private Long reorderLevel;

    @NotNull
    private Long reorderQuantity;

    @NotNull
    private Float sellingPrice;

    @NotNull
    private Long storeId;

    private Long productId;

    private Long optionDetailId;
}
