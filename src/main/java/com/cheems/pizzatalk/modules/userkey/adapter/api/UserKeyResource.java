package com.cheems.pizzatalk.modules.userkey.adapter.api;

import com.cheems.pizzatalk.modules.userkey.application.port.in.query.UserKeyCriteria;
import com.cheems.pizzatalk.modules.userkey.application.port.in.share.QueryUserKeyUseCase;
import com.cheems.pizzatalk.modules.userkey.domain.UserKey;
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
public class UserKeyResource {

    private final Logger log = LoggerFactory.getLogger(UserKeyResource.class);

    private final QueryUserKeyUseCase queryUserKeyUseCase;

    public UserKeyResource(QueryUserKeyUseCase queryUserKeyUseCase) {
        this.queryUserKeyUseCase = queryUserKeyUseCase;
    }

    @GetMapping("/user-keys/all")
    public ResponseEntity<List<UserKey>> getAllUserKeys(UserKeyCriteria criteria) {
        log.debug("REST request to get all user keys by criteria: {}", criteria);
        return ResponseEntity.ok().body(queryUserKeyUseCase.findListByCriteria(criteria));
    }

    @GetMapping("/user-keys")
    public ResponseEntity<List<UserKey>> getPageUserKeys(UserKeyCriteria criteria, Pageable pageable) {
        log.debug("REST request to get user keys by criteria: {}", criteria);
        Page<UserKey> page = queryUserKeyUseCase.findPageByCriteria(criteria, pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Total-Count", Long.toString(page.getTotalElements()));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/user-keys/{id}")
    public ResponseEntity<UserKey> getUserKey(@PathVariable(value = "id", required = true) Long id) {
        log.debug("REST request to get user key, ID: {}", id);
        Optional<UserKey> optionalUserKey = queryUserKeyUseCase.findById(id);
        return optionalUserKey
            .map(userKey -> ResponseEntity.ok().body(userKey))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
