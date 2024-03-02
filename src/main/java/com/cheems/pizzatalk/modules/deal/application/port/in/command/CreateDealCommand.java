package com.cheems.pizzatalk.modules.deal.application.port.in.command;

import com.cheems.pizzatalk.common.cqrs.CommandSelfValidating;
import com.cheems.pizzatalk.entities.enumeration.DealStatus;
import javax.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class CreateDealCommand extends CommandSelfValidating<CreateDealCommand> {

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    private String description;

    @NotNull
    private DealStatus status;

    @Size(max = 10)
    private String dealNo;

    private Float price;

    @NotNull
    @Size(max = 50)
    private String slug;

    @Size(max = 300)
    private String imagePath;

    private Long parentDealId;
}
