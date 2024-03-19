package com.cheems.pizzatalk.modules.product.application.port.in.share;

import com.cheems.pizzatalk.entities.enumeration.CommerceStatus;
import com.cheems.pizzatalk.modules.product.application.port.in.command.CreateProductCommand;
import com.cheems.pizzatalk.modules.product.application.port.in.command.UpdateProductCommand;
import com.cheems.pizzatalk.modules.product.domain.Product;

public interface ProductLifecycleUseCase {
    Product create(CreateProductCommand command);

    Product update(UpdateProductCommand command);

    void deleteById(Long id);

    Product updateCommerceStatus(Long id, CommerceStatus newStatus);
}
