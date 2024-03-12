package com.cheems.pizzatalk.modules.account.adapter.api;

import com.cheems.pizzatalk.modules.account.application.port.in.command.AccountResetPasswordCommand;
import com.cheems.pizzatalk.modules.account.application.port.in.share.AccountLifecycleUseCase;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    @Value("${spring.application.name}")
    private String applicationName;

    private final AccountLifecycleUseCase accountLifecycleUseCase;

    public AccountResource(AccountLifecycleUseCase accountLifecycleUseCase) {
        this.accountLifecycleUseCase = accountLifecycleUseCase;
    }

    @GetMapping("/accounts/activate")
    public ResponseEntity<Void> activateAccount(@RequestParam(value = "activationKey") String activationKey) {
        log.debug("REST request to activate account with activation key: {}", activationKey);
        accountLifecycleUseCase.activateAccount(activationKey);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/accounts/request-reset-password")
    public ResponseEntity<Void> requestResetPassword(@RequestParam(value = "email") String email) {
        log.debug("REST request to request reset password for account with email: {}", email);
        accountLifecycleUseCase.requestResetPassword(email);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/accounts/reset-password")
    public ResponseEntity<Void> resetPassword(
        @RequestParam(value = "resetKey") String resetKey,
        @Valid @RequestBody AccountResetPasswordCommand command
    ) {
        log.debug("REST request to reset password: {}", command);
        accountLifecycleUseCase.resetPassword(resetKey, command);

        return ResponseEntity.noContent().build();
    }
}
