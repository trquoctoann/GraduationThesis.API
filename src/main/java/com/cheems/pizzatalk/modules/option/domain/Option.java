package com.cheems.pizzatalk.modules.option.domain;

import com.cheems.pizzatalk.entities.enumeration.CommerceStatus;
import com.cheems.pizzatalk.modules.optiondetail.domain.OptionDetail;
import com.cheems.pizzatalk.modules.product.domain.Product;
import java.io.Serializable;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Option implements Serializable {

    private Long id;

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

    private Set<Product> products;

    private Set<OptionDetail> optionDetails;
}
