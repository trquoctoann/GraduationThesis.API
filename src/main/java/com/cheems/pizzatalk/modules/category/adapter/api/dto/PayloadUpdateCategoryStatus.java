package com.cheems.pizzatalk.modules.category.adapter.api.dto;

import com.cheems.pizzatalk.entities.enumeration.CommerceStatus;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PayloadUpdateCategoryStatus {

    private CommerceStatus newStatus;
}
