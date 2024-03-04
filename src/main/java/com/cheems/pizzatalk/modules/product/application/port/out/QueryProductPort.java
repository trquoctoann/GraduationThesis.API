package com.cheems.pizzatalk.modules.product.application.port.out;

import com.cheems.pizzatalk.modules.product.application.port.in.query.ProductCriteria;
import com.cheems.pizzatalk.modules.product.domain.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryProductPort {
    Optional<Product> findByCriteria(ProductCriteria criteria);

    List<Product> findListByCriteria(ProductCriteria criteria);

    Page<Product> findPageByCriteria(ProductCriteria criteria, Pageable pageable);
}
