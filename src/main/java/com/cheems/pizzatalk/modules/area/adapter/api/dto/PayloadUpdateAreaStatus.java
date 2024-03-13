package com.cheems.pizzatalk.modules.area.adapter.api.dto;

import com.cheems.pizzatalk.common.cqrs.CommandSelfValidating;
import com.cheems.pizzatalk.entities.enumeration.OperationalStatus;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class PayloadUpdateAreaStatus extends CommandSelfValidating<PayloadUpdateAreaStatus> {

    @NotNull
    private OperationalStatus status;
}
