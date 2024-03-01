package com.cheems.pizzatalk.modules.category.application.port.in.command;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class UpdateCategoryCommand extends CreateCategoryCommand {

    @NotNull
    private Long id;
}
