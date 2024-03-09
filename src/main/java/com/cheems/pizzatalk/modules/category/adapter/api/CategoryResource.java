package com.cheems.pizzatalk.modules.category.adapter.api;

import com.cheems.pizzatalk.entities.mapper.CategoryMapper;
import com.cheems.pizzatalk.modules.category.application.port.in.command.CreateCategoryCommand;
import com.cheems.pizzatalk.modules.category.application.port.in.command.UpdateCategoryCommand;
import com.cheems.pizzatalk.modules.category.application.port.in.query.CategoryCriteria;
import com.cheems.pizzatalk.modules.category.application.port.in.share.CategoryLifecycleUseCase;
import com.cheems.pizzatalk.modules.category.application.port.in.share.QueryCategoryUseCase;
import com.cheems.pizzatalk.modules.category.domain.Category;
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
public class CategoryResource {

    private final Logger log = LoggerFactory.getLogger(CategoryResource.class);

    private static final String ENTITY_NAME = "category";

    @Value("${spring.application.name}")
    private String applicationName;

    private final CategoryLifecycleUseCase categoryLifecycleUseCase;

    private final QueryCategoryUseCase queryCategoryUseCase;

    public CategoryResource(CategoryLifecycleUseCase categoryLifecycleUseCase, QueryCategoryUseCase queryCategoryUseCase) {
        this.categoryLifecycleUseCase = categoryLifecycleUseCase;
        this.queryCategoryUseCase = queryCategoryUseCase;
    }

    @GetMapping("/categories/all")
    public ResponseEntity<List<Category>> getAllCategories(CategoryCriteria criteria) {
        log.debug("REST request to get all categories by criteria: {}", criteria);
        return ResponseEntity.ok().body(queryCategoryUseCase.findListByCriteria(criteria));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getPageCategories(CategoryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get categories by criteria: {}", criteria);
        Page<Category> page = queryCategoryUseCase.findPageByCriteria(criteria, pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Total-Count", Long.toString(page.getTotalElements()));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable(value = "id", required = true) Long id) {
        log.debug("REST request to get category, ID: {}", id);
        Optional<Category> optionalCategory = queryCategoryUseCase.findById(id, CategoryMapper.DOMAIN_PRODUCT);
        return optionalCategory
            .map(category -> ResponseEntity.ok().body(category))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    // @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @PostMapping("/categories")
    public ResponseEntity<Void> createCategory(@Valid @RequestBody CreateCategoryCommand command) throws URISyntaxException {
        log.debug("REST request to create category: {}", command);
        Category category = categoryLifecycleUseCase.create(command);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("/api/categories/" + category.getId()));
        headers.add("X-applicationName-alert", "entity.creation.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + category.getId().toString());

        return ResponseEntity.created(new URI("/api/categories/" + category.getId())).headers(headers).build();
    }

    // @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @PutMapping("/categories/{id}")
    public ResponseEntity<Void> updateCategory(
        @PathVariable(value = "id", required = true) Long id,
        @Valid @RequestBody UpdateCategoryCommand command
    ) {
        log.debug("REST request to update category, ID: {}", id);
        Category category = categoryLifecycleUseCase.update(command);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-applicationName-alert", "entity.update.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + category.getId().toString());

        return ResponseEntity.noContent().headers(headers).build();
    }

    // @PreAuthorize("hasAnyAuthority('" + AuthorityConstants.ADMINISTRATOR + "')")
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable(value = "id", required = true) Long id) {
        log.debug("REST request to delete category, ID: {}", id);
        categoryLifecycleUseCase.deleteById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-applicationName-alert", "entity.delete.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + id.toString());

        return ResponseEntity.noContent().headers(headers).build();
    }
}
