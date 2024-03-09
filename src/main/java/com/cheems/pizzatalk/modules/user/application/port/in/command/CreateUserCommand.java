package com.cheems.pizzatalk.modules.user.application.port.in.command;

import com.cheems.pizzatalk.common.cqrs.CommandSelfValidating;
import com.cheems.pizzatalk.entities.enumeration.UserStatus;
import java.util.Set;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class CreateUserCommand extends CommandSelfValidating<CreateUserCommand> {

    @NotNull
    private String username;

    @NotNull
    private String rawPassword;

    private String firstName;

    private String lastName;

    @Email
    @NotNull
    private String email;

    private String imageURL;

    private UserStatus status;

    private String langKey;

    private Set<String> roleNames;
}
