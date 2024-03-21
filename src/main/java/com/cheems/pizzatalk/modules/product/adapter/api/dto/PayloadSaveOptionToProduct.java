package com.cheems.pizzatalk.modules.product.adapter.api.dto;

import java.util.Set;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PayloadSaveOptionToProduct {

    private Long optionId;

    private Set<Long> optionDetailIds;
}
