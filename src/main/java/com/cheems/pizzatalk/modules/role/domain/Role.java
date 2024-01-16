package com.cheems.pizzatalk.modules.role.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Role implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String authority;
}
