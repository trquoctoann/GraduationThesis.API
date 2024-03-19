package com.cheems.pizzatalk.modules.product.adapter.api.dto;

import com.cheems.pizzatalk.entities.enumeration.CommerceStatus;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PayloadUpdateProductStatus {

    private CommerceStatus newStatus;
}
