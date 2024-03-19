package com.cheems.pizzatalk.modules.product.application.port.in.share;

import com.cheems.pizzatalk.modules.product.application.port.in.query.ProductCriteria;
import com.cheems.pizzatalk.modules.product.domain.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryProductUseCase {
    Optional<Product> findById(Long id, String... fetchAttributes);

    Product getById(Long id, String... fetchAttributes);

    Optional<Product> findBySku(String sku, String... fetchAttributes);

    Product getBySku(String sku, String... fetchAttributes);

    Optional<Product> findByCriteria(ProductCriteria criteria);

    Product getByCriteria(ProductCriteria criteria);

    List<Product> findListByCriteria(ProductCriteria criteria);

    Page<Product> findPageByCriteria(ProductCriteria criteria, Pageable pageable);

    List<Product> findProductsByCategoryId(Long categoryId);
}
