package com.cheems.pizzatalk.modules.product.application.port.in.share;

import java.util.Set;

public interface ProductOptionUseCase {
    void saveOptionToProduct(Long productId, Long optionId, Set<Long> optionDetailIds);

    void removeAllOptionOfProduct(Long productId);
}
