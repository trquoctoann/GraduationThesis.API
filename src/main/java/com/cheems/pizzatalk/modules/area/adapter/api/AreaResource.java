package com.cheems.pizzatalk.modules.area.adapter.api;

import com.cheems.pizzatalk.entities.mapper.AreaMapper;
import com.cheems.pizzatalk.modules.area.application.port.in.command.CreateAreaCommand;
import com.cheems.pizzatalk.modules.area.application.port.in.command.UpdateAreaCommand;
import com.cheems.pizzatalk.modules.area.application.port.in.query.AreaCriteria;
import com.cheems.pizzatalk.modules.area.application.port.in.share.AreaLifecycleUseCase;
import com.cheems.pizzatalk.modules.area.application.port.in.share.QueryAreaUseCase;
import com.cheems.pizzatalk.modules.area.domain.Area;
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
public class AreaResource {

    private final Logger log = LoggerFactory.getLogger(AreaResource.class);

    private static final String ENTITY_NAME = "area";

    @Value("${spring.application.name}")
    private String applicationName;

    private final AreaLifecycleUseCase areaLifecycleUseCase;

    private final QueryAreaUseCase queryAreaUseCase;

    public AreaResource(AreaLifecycleUseCase areaLifecycleUseCase, QueryAreaUseCase queryAreaUseCase) {
        this.areaLifecycleUseCase = areaLifecycleUseCase;
        this.queryAreaUseCase = queryAreaUseCase;
    }

    @GetMapping("/areas/all")
    public ResponseEntity<List<Area>> getAllAreas(AreaCriteria criteria) {
        log.debug("REST request to get all areas by criteria: {}", criteria);
        return ResponseEntity.ok().body(queryAreaUseCase.findListByCriteria(criteria));
    }

    @GetMapping("/areas")
    public ResponseEntity<List<Area>> getPageAreas(AreaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get areas by criteria: {}", criteria);
        Page<Area> page = queryAreaUseCase.findPageByCriteria(criteria, pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Total-Count", Long.toString(page.getTotalElements()));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/areas/{id}")
    public ResponseEntity<Area> getArea(@PathVariable(value = "id", required = true) Long id) {
        log.debug("REST request to get area, ID: {}", id);
        Optional<Area> optionalArea = queryAreaUseCase.findById(id, AreaMapper.DOMAIN_STORE);
        return optionalArea
            .map(area -> ResponseEntity.ok().body(area))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    // @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @PostMapping("/areas")
    public ResponseEntity<Void> createArea(@Valid @RequestBody CreateAreaCommand command) throws URISyntaxException {
        log.debug("REST request to create area: {}", command);
        Area area = areaLifecycleUseCase.create(command);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("/api/users/" + area.getId()));
        headers.add("X-applicationName-alert", "entity.creation.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + area.getId().toString());

        return ResponseEntity.created(new URI("/api/areas/" + area.getId())).headers(headers).build();
    }

    // @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @PutMapping("/areas/{id}")
    public ResponseEntity<Void> updateArea(
        @PathVariable(value = "id", required = true) Long id,
        @Valid @RequestBody UpdateAreaCommand command
    ) {
        log.debug("REST request to update area, ID: {}", id);
        Area area = areaLifecycleUseCase.update(command);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-applicationName-alert", "entity.update.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + area.getId().toString());

        return ResponseEntity.noContent().headers(headers).build();
    }

    // @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @DeleteMapping("/areas/{id}")
    public ResponseEntity<Void> deleteArea(@PathVariable(value = "id", required = true) Long id) {
        log.debug("REST request to delete area, ID: {}", id);
        areaLifecycleUseCase.deleteById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-applicationName-alert", "entity.delete.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + id.toString());

        return ResponseEntity.noContent().headers(headers).build();
    }
}
