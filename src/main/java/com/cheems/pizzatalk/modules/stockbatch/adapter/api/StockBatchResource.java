package com.cheems.pizzatalk.modules.stockbatch.adapter.api;

import com.cheems.pizzatalk.entities.mapper.StockBatchMapper;
import com.cheems.pizzatalk.modules.stockbatch.application.port.in.command.CreateStockBatchCommand;
import com.cheems.pizzatalk.modules.stockbatch.application.port.in.command.UpdateStockBatchCommand;
import com.cheems.pizzatalk.modules.stockbatch.application.port.in.query.StockBatchCriteria;
import com.cheems.pizzatalk.modules.stockbatch.application.port.in.share.QueryStockBatchUseCase;
import com.cheems.pizzatalk.modules.stockbatch.application.port.in.share.StockBatchLifecycleUseCase;
import com.cheems.pizzatalk.modules.stockbatch.domain.StockBatch;
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
public class StockBatchResource {

    private final Logger log = LoggerFactory.getLogger(StockBatchResource.class);

    private static final String ENTITY_NAME = "stockBatch";

    @Value("${spring.application.name}")
    private String applicationName;

    private final StockBatchLifecycleUseCase stockBatchLifecycleUseCase;

    private final QueryStockBatchUseCase queryStockBatchUseCase;

    public StockBatchResource(StockBatchLifecycleUseCase stockBatchLifecycleUseCase, QueryStockBatchUseCase queryStockBatchUseCase) {
        this.stockBatchLifecycleUseCase = stockBatchLifecycleUseCase;
        this.queryStockBatchUseCase = queryStockBatchUseCase;
    }

    @GetMapping("/stock-batches/all")
    public ResponseEntity<List<StockBatch>> getAllStockBatches(StockBatchCriteria criteria) {
        log.debug("REST request to get all stock batches by criteria: {}", criteria);
        return ResponseEntity.ok().body(queryStockBatchUseCase.findListByCriteria(criteria));
    }

    @GetMapping("/stock-batches")
    public ResponseEntity<List<StockBatch>> getPageStockBatches(StockBatchCriteria criteria, Pageable pageable) {
        log.debug("REST request to get stock batches by criteria: {}", criteria);
        Page<StockBatch> page = queryStockBatchUseCase.findPageByCriteria(criteria, pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Total-Count", Long.toString(page.getTotalElements()));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/stock-batches/{id}")
    public ResponseEntity<StockBatch> getStockBatch(@PathVariable(value = "id", required = true) Long id) {
        log.debug("REST request to get stock batch, ID: {}", id);
        Optional<StockBatch> optionalStockBatch = queryStockBatchUseCase.findById(id, StockBatchMapper.DOMAIN_STOCK_ITEM);
        return optionalStockBatch
            .map(stockBatch -> ResponseEntity.ok().body(stockBatch))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/stock-batches")
    public ResponseEntity<Void> createStockBatch(@Valid @RequestBody CreateStockBatchCommand command) throws URISyntaxException {
        log.debug("REST request to create stock batch: {}", command);
        StockBatch stockBatch = stockBatchLifecycleUseCase.create(command);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("/api/stock-batches/" + stockBatch.getId()));
        headers.add("X-applicationName-alert", "entity.creation.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + stockBatch.getId().toString());

        return ResponseEntity.created(new URI("/api/stock-batches/" + stockBatch.getId())).headers(headers).build();
    }

    @PutMapping("/stock-batches/{id}")
    public ResponseEntity<Void> updatePriceStockBatch(
        @PathVariable(value = "id", required = true) Long id,
        @Valid @RequestBody UpdateStockBatchCommand command
    ) {
        log.debug("REST request to update price for stock batch: {}", id);
        StockBatch stockBatch = stockBatchLifecycleUseCase.update(command);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-applicationName-alert", "entity.update.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + stockBatch.getId().toString());

        return ResponseEntity.noContent().headers(headers).build();
    }

    @DeleteMapping("/stock-batches/{id}")
    public ResponseEntity<Void> deleteStockBatch(@PathVariable(value = "id", required = true) Long id) {
        log.debug("REST request to delete stock batch, ID: {}", id);
        stockBatchLifecycleUseCase.deleteById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-applicationName-alert", "entity.delete.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + id.toString());

        return ResponseEntity.noContent().headers(headers).build();
    }
}
