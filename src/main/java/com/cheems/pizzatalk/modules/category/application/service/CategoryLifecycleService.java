package com.cheems.pizzatalk.modules.category.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.entities.enumeration.CommerceStatus;
import com.cheems.pizzatalk.modules.category.application.port.in.command.CreateCategoryCommand;
import com.cheems.pizzatalk.modules.category.application.port.in.command.UpdateCategoryCommand;
import com.cheems.pizzatalk.modules.category.application.port.in.share.CategoryLifecycleUseCase;
import com.cheems.pizzatalk.modules.category.application.port.in.share.QueryCategoryUseCase;
import com.cheems.pizzatalk.modules.category.application.port.out.CategoryPort;
import com.cheems.pizzatalk.modules.category.domain.Category;
import com.cheems.pizzatalk.modules.product.application.port.in.share.ProductLifecycleUseCase;
import com.cheems.pizzatalk.modules.product.application.port.in.share.QueryProductUseCase;
import com.cheems.pizzatalk.modules.product.domain.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryLifecycleService implements CategoryLifecycleUseCase {

    private static final Logger log = LoggerFactory.getLogger(CategoryLifecycleService.class);

    private final ObjectMapper objectMapper;

    private final CategoryPort categoryPort;

    private final QueryCategoryUseCase queryCategoryUseCase;

    private final ProductLifecycleUseCase productLifecycleUseCase;

    private final QueryProductUseCase queryProductUseCase;

    public CategoryLifecycleService(
        ObjectMapper objectMapper,
        CategoryPort categoryPort,
        QueryCategoryUseCase queryCategoryUseCase,
        ProductLifecycleUseCase productLifecycleUseCase,
        QueryProductUseCase queryProductUseCase
    ) {
        this.objectMapper = objectMapper;
        this.categoryPort = categoryPort;
        this.queryCategoryUseCase = queryCategoryUseCase;
        this.productLifecycleUseCase = productLifecycleUseCase;
        this.queryProductUseCase = queryProductUseCase;
    }

    @Override
    public Category create(CreateCategoryCommand command) {
        log.debug("Creating category: {}", command);
        if (queryCategoryUseCase.findByName(command.getName()).isPresent()) {
            throw new BusinessException("Category name already exists");
        }

        Category category = objectMapper.convertValue(command, Category.class);
        category = categoryPort.save(category);

        log.debug("Created category: {}", command);
        return category;
    }

    @Override
    public Category update(UpdateCategoryCommand command) {
        log.debug("Updating category, id: {}", command.getId());
        Category existCategory = queryCategoryUseCase.getById(command.getId());

        Category category = objectMapper.convertValue(command, Category.class);
        category.setId(existCategory.getId());
        category.setName(existCategory.getName());
        category.setStatus(existCategory.getStatus());

        category = categoryPort.save(category);
        log.debug("Updated category, id: {}", command.getId());
        return category;
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Deleting category, id: {}", id);
        List<Product> productsInCategory = queryProductUseCase.findProductsByCategoryId(id);
        productsInCategory.forEach(product -> productLifecycleUseCase.deleteById(product.getId()));

        categoryPort.deleteById(id);
        log.debug("Deleted category, id: {}", id);
    }

    @Override
    public Category updateCommerceStatus(Long id, CommerceStatus newStatus) {
        log.debug("Updating commerce status of category, id: {}", id);
        Category existCategory = queryCategoryUseCase.getById(id);
        CommerceStatus oldStatus = existCategory.getStatus();

        if (
            newStatus.equals(CommerceStatus.UPCOMING) ||
            oldStatus.equals(CommerceStatus.DISCONTINUED) ||
            (oldStatus.equals(CommerceStatus.UPCOMING) && newStatus.equals(CommerceStatus.DISCONTINUED)) ||
            (oldStatus.equals(CommerceStatus.UPCOMING) && newStatus.equals(CommerceStatus.INACTIVE))
        ) {
            throw new BusinessException("Cannot change status from " + oldStatus + " to " + newStatus);
        }

        if (newStatus.equals(CommerceStatus.DISCONTINUED)) {
            List<Product> productsInCategory = queryProductUseCase.findProductsByCategoryId(existCategory.getId());

            productsInCategory
                .stream()
                .filter(product -> product.getParentProductId() == null)
                .forEach(product -> productLifecycleUseCase.updateCommerceStatus(product.getId(), CommerceStatus.DISCONTINUED));
        }

        existCategory.setStatus(newStatus);
        existCategory = categoryPort.save(existCategory);
        log.debug("Updated commerce status of category, id: {} from {} to {}", id, oldStatus, newStatus);
        return existCategory;
    }
}
