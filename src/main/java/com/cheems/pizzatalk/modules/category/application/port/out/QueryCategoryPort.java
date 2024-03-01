package com.cheems.pizzatalk.modules.category.application.port.out;

import com.cheems.pizzatalk.modules.category.application.port.in.query.CategoryCriteria;
import com.cheems.pizzatalk.modules.category.domain.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryCategoryPort {
    Optional<Category> findByCriteria(CategoryCriteria criteria);

    List<Category> findListByCriteria(CategoryCriteria criteria);

    Page<Category> findPageByCriteria(CategoryCriteria criteria, Pageable pageable);
}
