package com.cheems.pizzatalk.modules.category.domain;

import com.cheems.pizzatalk.entities.enumeration.CommerceStatus;
import com.cheems.pizzatalk.modules.product.domain.Product;
import java.io.Serializable;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Category implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull
    private CommerceStatus status;

    @Size(max = 300)
    private String imagePath;

    private Set<Product> products;
}
