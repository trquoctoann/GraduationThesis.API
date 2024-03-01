package com.cheems.pizzatalk.modules.category.adapter.database;

import com.cheems.pizzatalk.entities.mapper.CategoryMapper;
import com.cheems.pizzatalk.modules.category.application.port.out.CategoryPort;
import com.cheems.pizzatalk.modules.category.domain.Category;
import com.cheems.pizzatalk.repository.CategoryRepository;
import org.springframework.stereotype.Component;

@Component
public class CategoryAdapter implements CategoryPort {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    public CategoryAdapter(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Category save(Category category) {
        return categoryMapper.toDomain(categoryRepository.save(categoryMapper.toEntity(category)));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
