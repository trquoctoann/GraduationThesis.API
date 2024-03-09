package com.cheems.pizzatalk.modules.deal.adapter.api;

import com.cheems.pizzatalk.entities.mapper.DealMapper;
import com.cheems.pizzatalk.modules.deal.application.port.in.command.CreateDealCommand;
import com.cheems.pizzatalk.modules.deal.application.port.in.command.UpdateDealCommand;
import com.cheems.pizzatalk.modules.deal.application.port.in.query.DealCriteria;
import com.cheems.pizzatalk.modules.deal.application.port.in.share.DealLifecycleUseCase;
import com.cheems.pizzatalk.modules.deal.application.port.in.share.QueryDealUseCase;
import com.cheems.pizzatalk.modules.deal.domain.Deal;
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
public class DealResource {

    private final Logger log = LoggerFactory.getLogger(DealResource.class);

    private static final String ENTITY_NAME = "deal";

    @Value("${spring.application.name}")
    private String applicationName;

    private final DealLifecycleUseCase dealLifecycleUseCase;

    private final QueryDealUseCase queryDealUseCase;

    public DealResource(DealLifecycleUseCase dealLifecycleUseCase, QueryDealUseCase queryDealUseCase) {
        this.dealLifecycleUseCase = dealLifecycleUseCase;
        this.queryDealUseCase = queryDealUseCase;
    }

    @GetMapping("/deals/all")
    public ResponseEntity<List<Deal>> getAllDeals(DealCriteria criteria) {
        log.debug("REST request to get all deals by criteria: {}", criteria);
        return ResponseEntity.ok().body(queryDealUseCase.findListByCriteria(criteria));
    }

    @GetMapping("/deals")
    public ResponseEntity<List<Deal>> getPageDeals(DealCriteria criteria, Pageable pageable) {
        log.debug("REST request to get deals by criteria: {}", criteria);
        Page<Deal> page = queryDealUseCase.findPageByCriteria(criteria, pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Total-Count", Long.toString(page.getTotalElements()));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/deals/{id}")
    public ResponseEntity<Deal> getDeal(@PathVariable(value = "id", required = true) Long id) {
        log.debug("REST request to get deal, ID: {}", id);
        Optional<Deal> optionalDeal = queryDealUseCase.findById(id, DealMapper.DOMAIN_DEAL_VARIATION, DealMapper.DOMAIN_PARENT_DEAL);
        return optionalDeal
            .map(deal -> ResponseEntity.ok().body(deal))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    // @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @PostMapping("/deals")
    public ResponseEntity<Void> createDeal(@Valid @RequestBody CreateDealCommand command) throws URISyntaxException {
        log.debug("REST request to create deal: {}", command);
        Deal deal = dealLifecycleUseCase.create(command);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("/api/deals/" + deal.getId()));
        headers.add("X-applicationName-alert", "entity.creation.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + deal.getId().toString());

        return ResponseEntity.created(new URI("/api/deals/" + deal.getId())).headers(headers).build();
    }

    // @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @PutMapping("/deals/{id}")
    public ResponseEntity<Void> updateDeal(
        @PathVariable(value = "id", required = true) Long id,
        @Valid @RequestBody UpdateDealCommand command
    ) {
        log.debug("REST request to update deal, ID: {}", id);
        Deal deal = dealLifecycleUseCase.update(command);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-applicationName-alert", "entity.update.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + deal.getId().toString());

        return ResponseEntity.noContent().headers(headers).build();
    }

    // @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @DeleteMapping("/deals/{id}")
    public ResponseEntity<Void> deleteDeal(@PathVariable(value = "id", required = true) Long id) {
        log.debug("REST request to delete deal, ID: {}", id);
        dealLifecycleUseCase.deleteById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-applicationName-alert", "entity.delete.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + id.toString());

        return ResponseEntity.noContent().headers(headers).build();
    }
}
