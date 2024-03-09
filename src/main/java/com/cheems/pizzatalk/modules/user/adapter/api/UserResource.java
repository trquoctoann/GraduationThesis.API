package com.cheems.pizzatalk.modules.user.adapter.api;

import com.cheems.pizzatalk.entities.mapper.UserMapper;
import com.cheems.pizzatalk.modules.user.application.port.in.command.CreateUserCommand;
import com.cheems.pizzatalk.modules.user.application.port.in.command.UpdateUserCommand;
import com.cheems.pizzatalk.modules.user.application.port.in.query.UserCriteria;
import com.cheems.pizzatalk.modules.user.application.port.in.share.QueryUserUseCase;
import com.cheems.pizzatalk.modules.user.application.port.in.share.UserLifecycleUseCase;
import com.cheems.pizzatalk.modules.user.domain.User;
import com.cheems.pizzatalk.service.MailService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private static final String ENTITY_NAME = "user";

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.application.base-url}")
    private String baseUrl;

    private final UserLifecycleUseCase userLifecycleUseCase;

    private final QueryUserUseCase queryUserUseCase;

    private final MailService mailService;

    public UserResource(UserLifecycleUseCase userLifecycleUseCase, QueryUserUseCase queryUserUseCase, MailService mailService) {
        this.userLifecycleUseCase = userLifecycleUseCase;
        this.queryUserUseCase = queryUserUseCase;
        this.mailService = mailService;
    }

    // @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @GetMapping("/users/all")
    public ResponseEntity<List<User>> getAllUsers(UserCriteria criteria) {
        log.debug("REST request to get all users by criteria: {}", criteria);
        return ResponseEntity.ok().body(queryUserUseCase.findListByCriteria(criteria));
    }

    // @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @GetMapping("/users")
    public ResponseEntity<List<User>> getPageUsers(UserCriteria criteria, Pageable pageable) {
        log.debug("REST request to get users by criteria: {}", criteria);
        Page<User> page = queryUserUseCase.findPageByCriteria(criteria, pageable);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Total-Count", Long.toString(page.getTotalElements()));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    // @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable(value = "id", required = true) Long id) {
        log.debug("REST request to get user, ID: {}", id);
        Optional<User> optionalUser = queryUserUseCase.findById(id, UserMapper.DOMAIN_ROLE);
        return optionalUser
            .map(user -> ResponseEntity.ok().body(user))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    // @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @PostMapping("/users")
    public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUserCommand command) throws URISyntaxException {
        log.debug("REST request to create user: {}", command);
        User user = userLifecycleUseCase.create(command);
        mailService.sendEmail(
            command.getEmail(),
            "Welcome to PizzaTalk!",
            this.baseUrl + "/accounts/activate?key=" + user.getActivationKey()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("/api/users/" + user.getId()));
        headers.add("X-applicationName-alert", "entity.creation.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + user.getId().toString());

        return ResponseEntity.created(new URI("/api/users/" + user.getId())).headers(headers).build();
    }

    // @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @PutMapping("/users/{id}")
    public ResponseEntity<Void> updateUser(
        @PathVariable(value = "id", required = true) Long id,
        @Valid @RequestBody UpdateUserCommand command
    ) {
        log.debug("REST request to update user, ID: {}", id);
        User user = userLifecycleUseCase.update(command);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-applicationName-alert", "entity.update.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + user.getId().toString());

        return ResponseEntity.noContent().headers(headers).build();
    }

    // @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(value = "id", required = true) Long id) {
        log.debug("REST request to delete user, ID: {}", id);
        userLifecycleUseCase.deleteById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-applicationName-alert", "entity.delete.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + id.toString());

        return ResponseEntity.noContent().headers(headers).build();
    }
}
