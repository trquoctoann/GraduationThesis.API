package com.cheems.pizzatalk.modules.permission.domain;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Permission implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String name;
}
