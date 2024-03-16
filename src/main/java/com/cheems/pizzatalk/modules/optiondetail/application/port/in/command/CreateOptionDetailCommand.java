package com.cheems.pizzatalk.modules.optiondetail.application.port.in.command;

import com.cheems.pizzatalk.common.cqrs.CommandSelfValidating;
import com.cheems.pizzatalk.entities.enumeration.CommerceStatus;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class CreateOptionDetailCommand extends CommandSelfValidating<CreateOptionDetailCommand> {

    @NotNull
    @Size(max = 100)
    private String name;

    @Size(min = 6, max = 6)
    private String sku;

    @Size(max = 20)
    private String code;

    @Size(max = 20)
    private String uomId;

    @NotNull
    private CommerceStatus status;

    @NotNull
    private Long optionId;
}
