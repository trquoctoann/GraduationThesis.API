package com.cheems.pizzatalk.modules.category.application.port.in.share;

import com.cheems.pizzatalk.entities.enumeration.CommerceStatus;
import com.cheems.pizzatalk.modules.category.application.port.in.command.CreateCategoryCommand;
import com.cheems.pizzatalk.modules.category.application.port.in.command.UpdateCategoryCommand;
import com.cheems.pizzatalk.modules.category.domain.Category;

public interface CategoryLifecycleUseCase {
    Category create(CreateCategoryCommand command);

    Category update(UpdateCategoryCommand command);

    void deleteById(Long id);

    Category updateCommerceStatus(Long id, CommerceStatus newStatus);
}
