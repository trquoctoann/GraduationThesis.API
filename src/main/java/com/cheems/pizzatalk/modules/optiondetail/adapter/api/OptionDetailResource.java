package com.cheems.pizzatalk.modules.optiondetail.adapter.api;

import com.cheems.pizzatalk.entities.mapper.OptionDetailMapper;
import com.cheems.pizzatalk.modules.optiondetail.application.port.in.command.CreateOptionDetailCommand;
import com.cheems.pizzatalk.modules.optiondetail.application.port.in.command.UpdateOptionDetailCommand;
import com.cheems.pizzatalk.modules.optiondetail.application.port.in.query.OptionDetailCriteria;
import com.cheems.pizzatalk.modules.optiondetail.application.port.in.share.OptionDetailLifecycleUseCase;
import com.cheems.pizzatalk.modules.optiondetail.application.port.in.share.QueryOptionDetailUseCase;
import com.cheems.pizzatalk.modules.optiondetail.domain.OptionDetail;
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
public class OptionDetailResource {

    private final Logger log = LoggerFactory.getLogger(OptionDetailResource.class);

    private static final String ENTITY_NAME = "optionDetail";

    @Value("${spring.application.name}")
    private String applicationName;

    private final OptionDetailLifecycleUseCase optionDetailLifecycleUseCase;

    private final QueryOptionDetailUseCase queryOptionDetailUseCase;

    public OptionDetailResource(
        OptionDetailLifecycleUseCase optionDetailLifecycleUseCase,
        QueryOptionDetailUseCase queryOptionDetailUseCase
    ) {
        this.optionDetailLifecycleUseCase = optionDetailLifecycleUseCase;
        this.queryOptionDetailUseCase = queryOptionDetailUseCase;
    }

    @GetMapping("/option-details/all")
    public ResponseEntity<List<OptionDetail>> getAllOptionDetails(OptionDetailCriteria criteria) {
        log.debug("REST request to get all option details by criteria: {}", criteria);
        return ResponseEntity.ok().body(queryOptionDetailUseCase.findListByCriteria(criteria));
    }

    @GetMapping("/option-details")
    public ResponseEntity<List<OptionDetail>> getPageOptionDetails(OptionDetailCriteria criteria, Pageable pageable) {
        log.debug("REST request to get option details by criteria: {}", criteria);
        Page<OptionDetail> page = queryOptionDetailUseCase.findPageByCriteria(criteria, pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Total-Count", Long.toString(page.getTotalElements()));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/option-details/{id}")
    public ResponseEntity<OptionDetail> getOptionDetail(@PathVariable(value = "id", required = true) Long id) {
        log.debug("REST request to get option detail, ID: {}", id);
        Optional<OptionDetail> optionalOptionDetail = queryOptionDetailUseCase.findById(
            id,
            OptionDetailMapper.DOMAIN_OPTION,
            OptionDetailMapper.DOMAIN_STOCK_ITEMS
        );
        return optionalOptionDetail
            .map(optionDetail -> ResponseEntity.ok().body(optionDetail))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    // @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @PostMapping("/option-details")
    public ResponseEntity<Void> createOptionDetail(@Valid @RequestBody CreateOptionDetailCommand command) throws URISyntaxException {
        log.debug("REST request to create option detail: {}", command);
        OptionDetail optionDetail = optionDetailLifecycleUseCase.create(command);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("/api/option-details/" + optionDetail.getId()));
        headers.add("X-applicationName-alert", "entity.creation.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + optionDetail.getId().toString());

        return ResponseEntity.created(new URI("/api/option-details/" + optionDetail.getId())).headers(headers).build();
    }

    // @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @PutMapping("/option-details/{id}")
    public ResponseEntity<Void> updateOptionDetail(
        @PathVariable(value = "id", required = true) Long id,
        @Valid @RequestBody UpdateOptionDetailCommand command
    ) {
        log.debug("REST request to update option detail, ID: {}", id);
        OptionDetail optionDetail = optionDetailLifecycleUseCase.update(command);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-applicationName-alert", "entity.update.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + optionDetail.getId().toString());

        return ResponseEntity.noContent().headers(headers).build();
    }

    // @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @DeleteMapping("/option-details/{id}")
    public ResponseEntity<Void> deleteOptionDetail(@PathVariable(value = "id", required = true) Long id) {
        log.debug("REST request to delete option detail, ID: {}", id);
        optionDetailLifecycleUseCase.deleteById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-applicationName-alert", "entity.delete.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + id.toString());

        return ResponseEntity.noContent().headers(headers).build();
    }
}
