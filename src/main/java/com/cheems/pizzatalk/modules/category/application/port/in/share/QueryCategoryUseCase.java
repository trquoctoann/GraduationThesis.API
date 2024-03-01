package com.cheems.pizzatalk.modules.category.application.port.in.share;

import com.cheems.pizzatalk.modules.category.application.port.in.query.CategoryCriteria;
import com.cheems.pizzatalk.modules.category.domain.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryCategoryUseCase {
    Optional<Category> findById(Long id, String... fetchAttributes);

    Category getById(Long id, String... fetchAttributes);

    Optional<Category> findByName(String name, String... fetchAttributes);

    Category getByName(String name, String... fetchAttributes);

    Optional<Category> findByCriteria(CategoryCriteria criteria);

    Category getByCriteria(CategoryCriteria criteria);

    List<Category> findListByCriteria(CategoryCriteria criteria);

    Page<Category> findPageByCriteria(CategoryCriteria criteria, Pageable pageable);
}
