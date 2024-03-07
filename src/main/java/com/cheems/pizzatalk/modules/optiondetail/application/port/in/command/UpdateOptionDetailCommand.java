package com.cheems.pizzatalk.modules.optiondetail.application.port.in.command;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class UpdateOptionDetailCommand extends CreateOptionDetailCommand {

    @NotNull
    private Long id;
}
