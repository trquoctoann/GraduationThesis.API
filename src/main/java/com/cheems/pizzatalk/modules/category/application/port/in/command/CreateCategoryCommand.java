package com.cheems.pizzatalk.modules.category.application.port.in.command;

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
public class CreateCategoryCommand extends CommandSelfValidating<CreateCategoryCommand> {

    @NotNull
    @Size(max = 50)
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull
    private CommerceStatus status;

    @Size(max = 300)
    private String imagePath;
}
