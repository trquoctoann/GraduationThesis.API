package com.cheems.pizzatalk.modules.optiondetail.domain;

import com.cheems.pizzatalk.entities.enumeration.CommerceStatus;
import com.cheems.pizzatalk.modules.option.domain.Option;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OptionDetail implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String value;

    @Size(min = 6, max = 6)
    private String sku;

    @Size(max = 20)
    private String code;

    @Size(max = 20)
    private String uomId;

    @NotNull
    private CommerceStatus status;

    @NotNull
    private Float price;

    @NotNull
    private Long quantity;

    private Long optionId;

    private Option option;
}
