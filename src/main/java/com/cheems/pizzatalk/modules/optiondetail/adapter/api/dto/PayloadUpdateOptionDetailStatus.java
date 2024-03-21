package com.cheems.pizzatalk.modules.optiondetail.adapter.api.dto;

import com.cheems.pizzatalk.entities.enumeration.CommerceStatus;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PayloadUpdateOptionDetailStatus {

    private CommerceStatus newStatus;
}
