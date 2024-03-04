package com.cheems.pizzatalk.modules.product.adapter.database;

import com.cheems.pizzatalk.entities.mapper.ProductMapper;
import com.cheems.pizzatalk.modules.product.application.port.out.ProductPort;
import com.cheems.pizzatalk.modules.product.domain.Product;
import com.cheems.pizzatalk.repository.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class ProductAdapter implements ProductPort {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductAdapter(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Product save(Product product) {
        return productMapper.toDomain(productRepository.save(productMapper.toEntity(product)));
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
