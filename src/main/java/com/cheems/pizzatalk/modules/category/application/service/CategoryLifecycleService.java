package com.cheems.pizzatalk.modules.category.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.modules.category.application.port.in.command.CreateCategoryCommand;
import com.cheems.pizzatalk.modules.category.application.port.in.command.UpdateCategoryCommand;
import com.cheems.pizzatalk.modules.category.application.port.in.share.CategoryLifecycleUseCase;
import com.cheems.pizzatalk.modules.category.application.port.in.share.QueryCategoryUseCase;
import com.cheems.pizzatalk.modules.category.application.port.out.CategoryPort;
import com.cheems.pizzatalk.modules.category.domain.Category;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public CategoryLifecycleService(ObjectMapper objectMapper, CategoryPort categoryPort, QueryCategoryUseCase queryCategoryUseCase) {
        this.objectMapper = objectMapper;
        this.categoryPort = categoryPort;
        this.queryCategoryUseCase = queryCategoryUseCase;
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

        category = categoryPort.save(category);
        log.debug("Updated category, id: {}", command.getId());
        return category;
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Deleting category, id: {}", id);
        categoryPort.deleteById(id);
        log.debug("Deleted category, id: {}", id);
    }
}
