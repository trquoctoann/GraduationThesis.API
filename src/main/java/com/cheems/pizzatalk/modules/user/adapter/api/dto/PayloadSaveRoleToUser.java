package com.cheems.pizzatalk.modules.user.adapter.api.dto;

import java.util.Set;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PayloadSaveRoleToUser {

    private Set<String> roles;
}
