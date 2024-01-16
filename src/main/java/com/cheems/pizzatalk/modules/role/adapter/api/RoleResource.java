package com.cheems.pizzatalk.modules.role.adapter.api;

import com.cheems.pizzatalk.modules.role.application.port.in.command.CreateRoleCommand;
import com.cheems.pizzatalk.modules.role.application.port.in.command.UpdateRoleCommand;
import com.cheems.pizzatalk.modules.role.application.port.in.query.RoleCriteria;
import com.cheems.pizzatalk.modules.role.application.port.in.share.QueryRoleUseCase;
import com.cheems.pizzatalk.modules.role.application.port.in.share.RoleLifecycleUseCase;
import com.cheems.pizzatalk.modules.role.domain.Role;
import com.cheems.pizzatalk.security.AuthorityConstants;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RoleResource {

    private final Logger logger = LoggerFactory.getLogger(RoleResource.class);

    private static final String ENTITY_NAME = "role";

    @Value("${spring.application.name}")
    private String applicationName;

    private final RoleLifecycleUseCase roleLifecycleUseCase;

    private final QueryRoleUseCase queryRoleUseCase;

    public RoleResource(RoleLifecycleUseCase roleLifecycleUseCase, QueryRoleUseCase queryRoleUseCase) {
        this.roleLifecycleUseCase = roleLifecycleUseCase;
        this.queryRoleUseCase = queryRoleUseCase;
    }

    @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRoles(RoleCriteria criteria) {
        logger.debug("REST request to get all roles by criteria: {}", criteria);
        return ResponseEntity.ok().body(queryRoleUseCase.findListByCriteria(criteria));
    }

    @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @GetMapping("/roles/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable(value = "id", required = true) Long id) {
        logger.debug("REST request to get role, ID: {}", id);
        return ResponseEntity.ok().body(queryRoleUseCase.getById(id));
    }

    @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @PostMapping("/roles")
    public ResponseEntity<Void> createRole(@Valid @RequestBody CreateRoleCommand command) throws URISyntaxException {
        logger.debug("REST request to create role: {}", command);
        Role role = roleLifecycleUseCase.create(command);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("/api/roles/" + role.getId()));
        headers.add("X-applicationName-alert", "entity.creation.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + role.getId().toString());

        return ResponseEntity.created(new URI("/api/roles/" + role.getId())).headers(headers).build();
    }

    @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @PutMapping("/roles/{id}")
    public ResponseEntity<Void> updateRole(
        @PathVariable(value = "id", required = true) Long id,
        @Valid @RequestBody UpdateRoleCommand command
    ) {
        logger.debug("REST request to update role, ID: {}", id);
        Role role = roleLifecycleUseCase.update(command);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-applicationName-alert", "entity.update.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + role.getId().toString());

        return ResponseEntity.ok().headers(headers).build();
    }

    @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable(value = "id", required = true) Long id) {
        logger.debug("REST request to delete role, ID: {}", id);
        roleLifecycleUseCase.deleteById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-applicationName-alert", "entity.delete.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + id.toString());

        return ResponseEntity.noContent().headers(headers).build();
    }
}
