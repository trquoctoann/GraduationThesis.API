package com.cheems.pizzatalk.modules.user.domain;

import com.cheems.pizzatalk.constant.LoginConstants;
import com.cheems.pizzatalk.entities.enumeration.UserStatus;
import com.cheems.pizzatalk.modules.role.domain.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.*;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 20)
    @Pattern(regexp = LoginConstants.LOGIN_REGEX)
    private String username;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private String rawPassword;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @NotNull
    @Size(min = 5, max = 254)
    private String email;

    @Size(max = 300)
    private String imageURL;

    @NotNull
    private UserStatus status;

    @NotNull
    @Size(max = 5)
    private String langKey;

    private List<Role> roles;
}
