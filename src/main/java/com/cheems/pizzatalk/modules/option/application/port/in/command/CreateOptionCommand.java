package com.cheems.pizzatalk.modules.option.application.port.in.command;

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
public class CreateOptionCommand extends CommandSelfValidating<CreateOptionCommand> {

    @NotNull
    @Size(max = 30)
    private String name;

    @NotNull
    @Size(max = 10)
    private String code;

    @NotNull
    private CommerceStatus status;

    @NotNull
    private Boolean isMulti;

    @NotNull
    private Boolean isRequired;
}
