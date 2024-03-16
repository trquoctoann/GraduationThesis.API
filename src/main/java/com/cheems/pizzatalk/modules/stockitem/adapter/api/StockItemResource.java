package com.cheems.pizzatalk.modules.stockitem.adapter.api;

import com.cheems.pizzatalk.entities.mapper.StockItemMapper;
import com.cheems.pizzatalk.modules.stockitem.adapter.api.dto.PayloadUpdatePriceStockItem;
import com.cheems.pizzatalk.modules.stockitem.adapter.api.dto.PayloadUpdateQuantityStockItem;
import com.cheems.pizzatalk.modules.stockitem.application.port.in.command.CreateStockItemCommand;
import com.cheems.pizzatalk.modules.stockitem.application.port.in.query.StockItemCriteria;
import com.cheems.pizzatalk.modules.stockitem.application.port.in.share.QueryStockItemUseCase;
import com.cheems.pizzatalk.modules.stockitem.application.port.in.share.StockItemLifecycleUseCase;
import com.cheems.pizzatalk.modules.stockitem.domain.StockItem;
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
public class StockItemResource {

    private final Logger log = LoggerFactory.getLogger(StockItemResource.class);

    private static final String ENTITY_NAME = "stockItem";

    @Value("${spring.application.name}")
    private String applicationName;

    private final StockItemLifecycleUseCase stockItemLifecycleUseCase;

    private final QueryStockItemUseCase queryStockItemUseCase;

    public StockItemResource(StockItemLifecycleUseCase stockItemLifecycleUseCase, QueryStockItemUseCase queryStockItemUseCase) {
        this.stockItemLifecycleUseCase = stockItemLifecycleUseCase;
        this.queryStockItemUseCase = queryStockItemUseCase;
    }

    @GetMapping("/stock-items/all")
    public ResponseEntity<List<StockItem>> getAllStockItems(StockItemCriteria criteria) {
        log.debug("REST request to get all stock items by criteria: {}", criteria);
        return ResponseEntity.ok().body(queryStockItemUseCase.findListByCriteria(criteria));
    }

    @GetMapping("/stock-items")
    public ResponseEntity<List<StockItem>> getPageStockItems(StockItemCriteria criteria, Pageable pageable) {
        log.debug("REST request to get stock items by criteria: {}", criteria);
        Page<StockItem> page = queryStockItemUseCase.findPageByCriteria(criteria, pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Total-Count", Long.toString(page.getTotalElements()));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/stock-items/{id}")
    public ResponseEntity<StockItem> getStockItem(@PathVariable(value = "id", required = true) Long id) {
        log.debug("REST request to get stock item, ID: {}", id);
        Optional<StockItem> optionalStockItem = queryStockItemUseCase.findById(
            id,
            StockItemMapper.DOMAIN_STORE,
            StockItemMapper.DOMAIN_PRODUCT,
            StockItemMapper.DOMAIN_OPTION_DETAIL,
            StockItemMapper.DOMAIN_STOCK_BATCHES
        );
        return optionalStockItem
            .map(stockItem -> ResponseEntity.ok().body(stockItem))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/stock-items")
    public ResponseEntity<Void> createStockItem(@Valid @RequestBody CreateStockItemCommand command) throws URISyntaxException {
        log.debug("REST request to create stock item: {}", command);
        StockItem stockItem = stockItemLifecycleUseCase.create(command);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("/api/stock-items/" + stockItem.getId()));
        headers.add("X-applicationName-alert", "entity.creation.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + stockItem.getId().toString());

        return ResponseEntity.created(new URI("/api/stock-items/" + stockItem.getId())).headers(headers).build();
    }

    @PutMapping("/stock-items/{id}/update-price")
    public ResponseEntity<Void> updatePriceStockItem(
        @PathVariable(value = "id", required = true) Long id,
        @RequestBody PayloadUpdatePriceStockItem payload
    ) {
        log.debug("REST request to update price for stock item: {}", id);
        StockItem stockItem = stockItemLifecycleUseCase.updatePrice(id, payload.getNewPrice());

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-applicationName-alert", "entity.update.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + stockItem.getId().toString());

        return ResponseEntity.noContent().headers(headers).build();
    }

    @PutMapping("/stock-items/{id}/update-quantity")
    public ResponseEntity<Void> updateQuantityStockItem(
        @PathVariable(value = "id", required = true) Long id,
        @RequestBody PayloadUpdateQuantityStockItem payload
    ) {
        log.debug("REST request to update price for stock item: {}", id);
        StockItem stockItem = stockItemLifecycleUseCase.updateQuantity(id, payload.getNewQuantity());

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-applicationName-alert", "entity.update.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + stockItem.getId().toString());

        return ResponseEntity.noContent().headers(headers).build();
    }

    @DeleteMapping("/stock-items/{id}")
    public ResponseEntity<Void> deleteStockItem(@PathVariable(value = "id", required = true) Long id) {
        log.debug("REST request to delete stock item, ID: {}", id);
        stockItemLifecycleUseCase.deleteById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-applicationName-alert", "entity.delete.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + id.toString());

        return ResponseEntity.noContent().headers(headers).build();
    }
}
