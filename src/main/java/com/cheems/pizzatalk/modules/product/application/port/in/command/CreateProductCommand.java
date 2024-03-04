package com.cheems.pizzatalk.modules.product.application.port.in.command;

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
public class CreateProductCommand extends CommandSelfValidating<CreateProductCommand> {

    @NotNull
    @Size(max = 100)
    private String name;

    @Size(max = 2)
    private String size;

    @NotNull
    @Size(max = 50)
    private String slug;

    private String description;

    @NotNull
    @Size(min = 6, max = 6)
    private String sku;

    @NotNull
    private CommerceStatus status;

    private Float price;

    private Long quantity;

    @Size(max = 300)
    private String imagePath;

    private Long parentProductId;

    @NotNull
    private Long categoryId;
}
