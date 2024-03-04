package com.cheems.pizzatalk.modules.product.application.port.out;

import com.cheems.pizzatalk.modules.product.domain.Product;

public interface ProductPort {
    Product save(Product product);

    void deleteById(Long id);
}
