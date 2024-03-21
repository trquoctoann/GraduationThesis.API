package com.cheems.pizzatalk.modules.product.application.port.out;

import java.util.Set;

import com.cheems.pizzatalk.modules.product.domain.Product;

public interface ProductPort {
    Product save(Product product);

    void saveOptionDetail(Long productId, Long optionId, Set<Long> optionDetailIds);

    void removeOptionDetail(Long productId, Long optionId, Set<Long> optionDetailIds);

    void removeAllOptionOfProduct(Long productId);

    void deleteById(Long id);
}
