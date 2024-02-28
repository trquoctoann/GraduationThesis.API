package com.cheems.pizzatalk.modules.area.domain;

import com.cheems.pizzatalk.entities.enumeration.OperationalStatus;
import com.cheems.pizzatalk.modules.store.domain.Store;
import java.io.Serializable;
import java.util.Set;
import javax.validation.constraints.*;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Area implements Serializable {

    private Long id;

    private Long originalId;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    @Size(max = 5)
    private String code;

    @NotNull
    @Size(max = 20)
    private String brandCode;

    @NotNull
    private OperationalStatus status;

    @NotNull
    private Long storeCount;

    @NotNull
    private String priceGroupId;

    private Set<Store> stores;
}
