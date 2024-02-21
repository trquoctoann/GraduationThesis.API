package com.cheems.pizzatalk.modules.account.adapter.api;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.modules.account.application.port.in.share.AccountLifecycleUseCase;
import com.cheems.pizzatalk.modules.user.application.port.in.command.ResetPasswordUserPasswordCommand;
import com.cheems.pizzatalk.modules.user.domain.User;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private static final String ENTITY_NAME = "user";

    @Value("${spring.application.name}")
    private String applicationName;

    private final AccountLifecycleUseCase accountLifecycleUseCase;

    public AccountResource(AccountLifecycleUseCase accountLifecycleUseCase) {
        this.accountLifecycleUseCase = accountLifecycleUseCase;
    }

    @PostMapping("/accounts/activate")
    public ResponseEntity<Void> activateAccount(@RequestParam(value = "activationKey") String activationKey) {
        log.debug("REST request to activate account with activation key: {}", activationKey);
        Optional<User> user = accountLifecycleUseCase.activateAccount(activationKey);
        if (!user.isPresent()) {
            throw new BusinessException("The activation key you provided does not exist. Please check and try again.");
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/accounts/request-reset-password")
    public ResponseEntity<Void> requestResetPassword(@RequestParam(value = "email") String email) {
        log.debug("REST request to request reset password for account with email: {}", email);
        Optional<User> user = accountLifecycleUseCase.requestResetPassword(email);
        if (!user.isPresent()) {
            throw new BusinessException("The email address you entered does not exist in our system. Please try again.");
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/accounts/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordUserPasswordCommand command) {
        log.debug("REST request to reset password: {}", command);
        Optional<User> user = accountLifecycleUseCase.resetPassword(command);
        if (!user.isPresent()) {
            throw new BusinessException("The reset key you provided does not exist. Please check and try again.");
        }
        return ResponseEntity.noContent().build();
    }
}
