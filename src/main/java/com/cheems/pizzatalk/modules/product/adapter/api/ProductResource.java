package com.cheems.pizzatalk.modules.product.adapter.api;

import com.cheems.pizzatalk.entities.mapper.ProductMapper;
import com.cheems.pizzatalk.modules.product.adapter.api.dto.PayloadUpdateProductStatus;
import com.cheems.pizzatalk.modules.product.application.port.in.command.CreateProductCommand;
import com.cheems.pizzatalk.modules.product.application.port.in.command.UpdateProductCommand;
import com.cheems.pizzatalk.modules.product.application.port.in.query.ProductCriteria;
import com.cheems.pizzatalk.modules.product.application.port.in.share.ProductLifecycleUseCase;
import com.cheems.pizzatalk.modules.product.application.port.in.share.QueryProductUseCase;
import com.cheems.pizzatalk.modules.product.domain.Product;
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
public class ProductResource {

    private final Logger log = LoggerFactory.getLogger(ProductResource.class);

    private static final String ENTITY_NAME = "product";

    @Value("${spring.application.name}")
    private String applicationName;

    private final ProductLifecycleUseCase productLifecycleUseCase;

    private final QueryProductUseCase queryProductUseCase;

    public ProductResource(ProductLifecycleUseCase productLifecycleUseCase, QueryProductUseCase queryProductUseCase) {
        this.productLifecycleUseCase = productLifecycleUseCase;
        this.queryProductUseCase = queryProductUseCase;
    }

    @GetMapping("/products/all")
    public ResponseEntity<List<Product>> getAllProducts(ProductCriteria criteria) {
        log.debug("REST request to get all products by criteria: {}", criteria);
        return ResponseEntity.ok().body(queryProductUseCase.findListByCriteria(criteria));
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getPageProducts(ProductCriteria criteria, Pageable pageable) {
        log.debug("REST request to get products by criteria: {}", criteria);
        Page<Product> page = queryProductUseCase.findPageByCriteria(criteria, pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Total-Count", Long.toString(page.getTotalElements()));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable(value = "id", required = true) Long id) {
        log.debug("REST request to get product, ID: {}", id);
        Optional<Product> optionalProduct = queryProductUseCase.findById(
            id,
            ProductMapper.DOMAIN_CATEGORY,
            ProductMapper.DOMAIN_OPTIONS,
            ProductMapper.DOMAIN_PARENT_PRODUCT,
            ProductMapper.DOMAIN_PRODUCT_VARIATIONS,
            ProductMapper.DOMAIN_OPTION_DETAILS,
            ProductMapper.DOMAIN_STOCK_ITEMS
        );
        return optionalProduct
            .map(product -> ResponseEntity.ok().body(product))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    // @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @PostMapping("/products")
    public ResponseEntity<Void> createProduct(@Valid @RequestBody CreateProductCommand command) throws URISyntaxException {
        log.debug("REST request to create product: {}", command);
        Product product = productLifecycleUseCase.create(command);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("/api/products/" + product.getId()));
        headers.add("X-applicationName-alert", "entity.creation.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + product.getId().toString());

        return ResponseEntity.created(new URI("/api/products/" + product.getId())).headers(headers).build();
    }

    // @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @PutMapping("/products/{id}")
    public ResponseEntity<Void> updateProduct(
        @PathVariable(value = "id", required = true) Long id,
        @Valid @RequestBody UpdateProductCommand command
    ) {
        log.debug("REST request to update product, ID: {}", id);
        Product product = productLifecycleUseCase.update(command);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-applicationName-alert", "entity.update.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + product.getId().toString());

        return ResponseEntity.noContent().headers(headers).build();
    }

    // @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(value = "id", required = true) Long id) {
        log.debug("REST request to delete product, ID: {}", id);
        productLifecycleUseCase.deleteById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-applicationName-alert", "entity.delete.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + id.toString());

        return ResponseEntity.noContent().headers(headers).build();
    }

    // @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @PutMapping("/products/{id}/update-status")
    public ResponseEntity<Void> updateProductCommerceStatus(
        @PathVariable(value = "id", required = true) Long id,
        @RequestBody PayloadUpdateProductStatus payload
    ) {
        log.debug("REST request to update status of product, ID: {}", id);
        productLifecycleUseCase.updateCommerceStatus(id, payload.getNewStatus());
        return ResponseEntity.noContent().build();
    }
}
