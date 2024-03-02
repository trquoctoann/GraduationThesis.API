package com.cheems.pizzatalk.modules.deal.application.port.in.command;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class UpdateDealCommand extends CreateDealCommand {

    @NotNull
    private Long id;
}
