package com.cheems.pizzatalk.modules.category.application.port.out;

import com.cheems.pizzatalk.modules.category.domain.Category;

public interface CategoryPort {
    Category save(Category category);

    void deleteById(Long id);
}
