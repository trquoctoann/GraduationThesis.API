package com.cheems.pizzatalk.modules.option.adapter.api;

import com.cheems.pizzatalk.entities.mapper.OptionMapper;
import com.cheems.pizzatalk.modules.option.application.port.in.command.CreateOptionCommand;
import com.cheems.pizzatalk.modules.option.application.port.in.command.UpdateOptionCommand;
import com.cheems.pizzatalk.modules.option.application.port.in.query.OptionCriteria;
import com.cheems.pizzatalk.modules.option.application.port.in.share.OptionLifecycleUseCase;
import com.cheems.pizzatalk.modules.option.application.port.in.share.QueryOptionUseCase;
import com.cheems.pizzatalk.modules.option.domain.Option;
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
public class OptionResource {

    private final Logger log = LoggerFactory.getLogger(OptionResource.class);

    private static final String ENTITY_NAME = "option";

    @Value("${spring.application.name}")
    private String applicationName;

    private final OptionLifecycleUseCase optionLifecycleUseCase;

    private final QueryOptionUseCase queryOptionUseCase;

    public OptionResource(OptionLifecycleUseCase optionLifecycleUseCase, QueryOptionUseCase queryOptionUseCase) {
        this.optionLifecycleUseCase = optionLifecycleUseCase;
        this.queryOptionUseCase = queryOptionUseCase;
    }

    @GetMapping("/options/all")
    public ResponseEntity<List<Option>> getAllOptions(OptionCriteria criteria) {
        log.debug("REST request to get all options by criteria: {}", criteria);
        return ResponseEntity.ok().body(queryOptionUseCase.findListByCriteria(criteria));
    }

    @GetMapping("/options")
    public ResponseEntity<List<Option>> getPageOptions(OptionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get options by criteria: {}", criteria);
        Page<Option> page = queryOptionUseCase.findPageByCriteria(criteria, pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Total-Count", Long.toString(page.getTotalElements()));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/options/{id}")
    public ResponseEntity<Option> getOption(@PathVariable(value = "id", required = true) Long id) {
        log.debug("REST request to get option, ID: {}", id);
        Optional<Option> optionalOption = queryOptionUseCase.findById(id, OptionMapper.DOMAIN_PRODUCTS, OptionMapper.DOMAIN_OPTION_DETAILS);
        return optionalOption
            .map(option -> ResponseEntity.ok().body(option))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    // @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @PostMapping("/options")
    public ResponseEntity<Void> createOption(@Valid @RequestBody CreateOptionCommand command) throws URISyntaxException {
        log.debug("REST request to create option: {}", command);
        Option option = optionLifecycleUseCase.create(command);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("/api/options/" + option.getId()));
        headers.add("X-applicationName-alert", "entity.creation.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + option.getId().toString());

        return ResponseEntity.created(new URI("/api/options/" + option.getId())).headers(headers).build();
    }

    // @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @PutMapping("/options/{id}")
    public ResponseEntity<Void> updateOption(
        @PathVariable(value = "id", required = true) Long id,
        @Valid @RequestBody UpdateOptionCommand command
    ) {
        log.debug("REST request to update option, ID: {}", id);
        Option option = optionLifecycleUseCase.update(command);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-applicationName-alert", "entity.update.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + option.getId().toString());

        return ResponseEntity.noContent().headers(headers).build();
    }

    // @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @DeleteMapping("/options/{id}")
    public ResponseEntity<Void> deleteOption(@PathVariable(value = "id", required = true) Long id) {
        log.debug("REST request to delete option, ID: {}", id);
        optionLifecycleUseCase.deleteById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-applicationName-alert", "entity.delete.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + id.toString());

        return ResponseEntity.noContent().headers(headers).build();
    }
}
