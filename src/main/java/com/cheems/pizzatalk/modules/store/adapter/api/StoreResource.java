package com.cheems.pizzatalk.modules.store.adapter.api;

import com.cheems.pizzatalk.entities.mapper.StoreMapper;
import com.cheems.pizzatalk.modules.store.application.port.in.command.CreateStoreCommand;
import com.cheems.pizzatalk.modules.store.application.port.in.command.UpdateStoreCommand;
import com.cheems.pizzatalk.modules.store.application.port.in.query.StoreCriteria;
import com.cheems.pizzatalk.modules.store.application.port.in.share.QueryStoreUseCase;
import com.cheems.pizzatalk.modules.store.application.port.in.share.StoreLifecycleUseCase;
import com.cheems.pizzatalk.modules.store.domain.Store;
import com.cheems.pizzatalk.security.AuthorityConstants;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class StoreResource {

    private final Logger log = LoggerFactory.getLogger(StoreResource.class);

    private static final String ENTITY_NAME = "store";

    @Value("${spring.application.name}")
    private String applicationName;

    private final StoreLifecycleUseCase storeLifecycleUseCase;

    private final QueryStoreUseCase queryStoreUseCase;

    public StoreResource(StoreLifecycleUseCase storeLifecycleUseCase, QueryStoreUseCase queryStoreUseCase) {
        this.storeLifecycleUseCase = storeLifecycleUseCase;
        this.queryStoreUseCase = queryStoreUseCase;
    }

    @GetMapping("/stores/all")
    public ResponseEntity<List<Store>> getAllStores(StoreCriteria criteria) {
        log.debug("REST request to get all stores by criteria: {}", criteria);
        return ResponseEntity.ok().body(queryStoreUseCase.findListByCriteria(criteria));
    }

    @GetMapping("/stores")
    public ResponseEntity<List<Store>> getPageStores(StoreCriteria criteria, Pageable pageable) {
        log.debug("REST request to get stores by criteria: {}", criteria);
        Page<Store> page = queryStoreUseCase.findPageByCriteria(criteria, pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Total-Count", Long.toString(page.getTotalElements()));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/stores/{id}")
    public ResponseEntity<Store> getStore(@PathVariable(value = "id", required = true) Long id) {
        log.debug("REST request to get store, ID: {}", id);
        Optional<Store> optionalStore = queryStoreUseCase.findById(id, StoreMapper.DOMAIN_AREA);
        return optionalStore
            .map(store -> ResponseEntity.ok().body(store))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    // @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @PostMapping("/stores")
    public ResponseEntity<Void> createStore(@Valid @RequestBody CreateStoreCommand command) throws URISyntaxException {
        log.debug("REST request to create store: {}", command);
        Store store = storeLifecycleUseCase.create(command);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("/api/users/" + store.getId()));
        headers.add("X-applicationName-alert", "entity.creation.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + store.getId().toString());

        return ResponseEntity.created(new URI("/api/stores/" + store.getId())).headers(headers).build();
    }

    // @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @PutMapping("/stores/{id}")
    public ResponseEntity<Void> updateStore(
        @PathVariable(value = "id", required = true) Long id,
        @Valid @RequestBody UpdateStoreCommand command
    ) {
        log.debug("REST request to update store, ID: {}", id);
        Store store = storeLifecycleUseCase.update(command);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-applicationName-alert", "entity.update.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + store.getId().toString());

        return ResponseEntity.noContent().headers(headers).build();
    }

    // @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @DeleteMapping("/stores/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable(value = "id", required = true) Long id) {
        log.debug("REST request to delete store, ID: {}", id);
        storeLifecycleUseCase.deleteById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-applicationName-alert", "entity.delete.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + id.toString());

        return ResponseEntity.noContent().headers(headers).build();
    }
}
