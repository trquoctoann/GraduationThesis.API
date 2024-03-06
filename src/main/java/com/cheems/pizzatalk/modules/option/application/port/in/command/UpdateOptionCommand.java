package com.cheems.pizzatalk.modules.option.application.port.in.command;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class UpdateOptionCommand extends CreateOptionCommand {

    @NotNull
    private Long id;
}
