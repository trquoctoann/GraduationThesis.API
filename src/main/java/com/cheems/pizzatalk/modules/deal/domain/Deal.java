package com.cheems.pizzatalk.modules.deal.domain;

import com.cheems.pizzatalk.entities.enumeration.DealStatus;
import java.io.Serializable;
import java.util.Set;
import javax.validation.constraints.*;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Deal implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    private String description;

    @NotNull
    private DealStatus status;

    @Size(max = 10)
    private String dealNo;

    private Float price;

    @NotNull
    @Size(max = 50)
    private String slug;

    @Size(max = 300)
    private String imagePath;

    private Long parentDealId;

    private Deal parentDeal;

    private Set<Deal> dealVariations;
}
