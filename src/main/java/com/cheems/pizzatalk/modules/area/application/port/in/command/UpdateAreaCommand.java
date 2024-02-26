package com.cheems.pizzatalk.modules.area.application.port.in.command;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class UpdateAreaCommand extends CreateAreaCommand {

    @NotNull
    private Long id;
}
