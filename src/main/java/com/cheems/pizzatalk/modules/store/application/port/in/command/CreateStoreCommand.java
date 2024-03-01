package com.cheems.pizzatalk.modules.store.application.port.in.command;

import com.cheems.pizzatalk.common.cqrs.CommandSelfValidating;
import com.cheems.pizzatalk.entities.enumeration.OperationalStatus;
import javax.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class CreateStoreCommand extends CommandSelfValidating<CreateStoreCommand> {

    @NotNull
    @Size(max = 200)
    private String name;

    @NotNull
    @Size(max = 1000)
    private String address;

    @NotNull
    @Size(max = 15)
    private String phoneNumber;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    @NotNull
    private OperationalStatus status;

    @NotNull
    private Boolean allowDelivery;

    @NotNull
    private Boolean allowPickup;

    @Size(min = 2, max = 2)
    private String country;

    @Size(max = 5)
    private String state;

    @Size(max = 5)
    private String district;

    private Double longitude;

    private Double latitude;

    @Size(max = 20)
    private String openingHour;

    @Size(max = 300)
    private String imagePath;

    @NotNull
    private Long areaId;
}
