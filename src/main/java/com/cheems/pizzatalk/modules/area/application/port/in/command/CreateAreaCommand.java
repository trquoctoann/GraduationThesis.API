package com.cheems.pizzatalk.modules.area.application.port.in.command;

import com.cheems.pizzatalk.common.cqrs.CommandSelfValidating;
import com.cheems.pizzatalk.entities.enumeration.OperationalStatus;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class CreateAreaCommand extends CommandSelfValidating<CreateAreaCommand> {

    @NotNull
    private Long originalId;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    @Size(max = 5)
    private String code;

    @NotNull
    @Size(max = 20)
    private String brandCode;

    @NotNull
    private OperationalStatus status;

    @NotNull
    private Long storeCount;

    @NotNull
    private String priceGroupId;
}
