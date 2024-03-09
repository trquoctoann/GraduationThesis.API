package com.cheems.pizzatalk.modules.permission.adapter.api;

import com.cheems.pizzatalk.modules.permission.application.port.in.query.PermissionCriteria;
import com.cheems.pizzatalk.modules.permission.application.port.in.share.QueryPermissionUseCase;
import com.cheems.pizzatalk.modules.permission.domain.Permission;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class PermissionResource {

    private final Logger log = LoggerFactory.getLogger(PermissionResource.class);

    private final QueryPermissionUseCase queryPermissionUseCase;

    public PermissionResource(QueryPermissionUseCase queryPermissionUseCase) {
        this.queryPermissionUseCase = queryPermissionUseCase;
    }

    @GetMapping("/permissions/all")
    public ResponseEntity<List<Permission>> getAllPermissions(PermissionCriteria criteria) {
        log.debug("REST request to get permissions by criteria: {}", criteria);
        return ResponseEntity.ok().body(queryPermissionUseCase.findListByCriteria(criteria));
    }

    @GetMapping("/permissions")
    public ResponseEntity<List<Permission>> getPagePermissions(PermissionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get permissions by criteria: {}", criteria);
        Page<Permission> page = queryPermissionUseCase.findPageByCriteria(criteria, pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Total-Count", Long.toString(page.getTotalElements()));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/permissions/{id}")
    public ResponseEntity<Permission> getPermission(@PathVariable(value = "id", required = true) Long id) {
        log.debug("REST request to get permission, ID: {}", id);
        Optional<Permission> optionalPermission = queryPermissionUseCase.findById(id);
        return optionalPermission
            .map(permission -> ResponseEntity.ok().body(permission))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
