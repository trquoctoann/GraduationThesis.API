package com.cheems.pizzatalk.modules.role.adapter.api.dto;

import java.util.Set;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PayloadSavePermissionToRole {

    private Set<String> permissions;
}
