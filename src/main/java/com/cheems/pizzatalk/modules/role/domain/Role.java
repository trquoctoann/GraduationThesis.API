package com.cheems.pizzatalk.modules.role.domain;

import com.cheems.pizzatalk.modules.permission.domain.Permission;
import java.io.Serializable;
import java.util.Set;
import javax.validation.constraints.*;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Role implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String name;

    private Set<String> permissions;
}
