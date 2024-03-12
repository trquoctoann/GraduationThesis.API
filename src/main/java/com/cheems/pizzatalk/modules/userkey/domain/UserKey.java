package com.cheems.pizzatalk.modules.userkey.domain;

import com.cheems.pizzatalk.entities.enumeration.UserKeyStatus;
import com.cheems.pizzatalk.entities.enumeration.UserKeyType;
import com.cheems.pizzatalk.modules.user.domain.User;
import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserKey implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 36)
    private String value;

    @NotNull
    private UserKeyType type;

    @NotNull
    private UserKeyStatus status;

    private Instant creationDate;

    private Instant usedDate;

    private Instant expirationDate;

    private Long userId;

    private User user;
}
