package com.cheems.pizzatalk.modules.product.adapter.database;

import com.cheems.pizzatalk.entities.ProductOptionEntity;
import com.cheems.pizzatalk.entities.mapper.OptionDetailMapper;
import com.cheems.pizzatalk.entities.mapper.OptionMapper;
import com.cheems.pizzatalk.entities.mapper.ProductMapper;
import com.cheems.pizzatalk.modules.product.application.port.out.ProductPort;
import com.cheems.pizzatalk.modules.product.domain.Product;
import com.cheems.pizzatalk.repository.ProductOptionRepository;
import com.cheems.pizzatalk.repository.ProductRepository;

import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class ProductAdapter implements ProductPort {

    private final ProductRepository productRepository;

    private final ProductOptionRepository productOptionRepository;

    private final ProductMapper productMapper;

    private final OptionMapper optionMapper;

    private final OptionDetailMapper optionDetailMapper;

    public ProductAdapter(ProductRepository productRepository, ProductOptionRepository productOptionRepository, ProductMapper productMapper, OptionMapper optionMapper, OptionDetailMapper optionDetailMapper) {
        this.productRepository = productRepository;
        this.productOptionRepository = productOptionRepository;
        this.productMapper = productMapper;
        this.optionMapper = optionMapper;
        this.optionDetailMapper = optionDetailMapper;
    }

    @Override
    public Product save(Product product) {
        return productMapper.toDomain(productRepository.save(productMapper.toEntity(product)));
    }

    @Override
    public void saveOptionDetail(Long productId, Long optionId, Set<Long> optionDetailIds) {
        optionDetailIds.forEach(
            optionDetailId -> {
                ProductOptionEntity productOptionEntity = new ProductOptionEntity();
                productOptionEntity.setProduct(productMapper.fromId(productId));
                productOptionEntity.setOption(optionMapper.fromId(optionId));
                productOptionEntity.setOptionDetail(optionDetailMapper.fromId(optionDetailId));
                productOptionRepository.save(productOptionEntity);
            }
        );
    }

    @Override
    public void removeOptionDetail(Long productId, Long optionId, Set<Long> optionDetailIds) {
        Set<ProductOptionEntity> existProductOptionEntities = productOptionRepository.findByProductIdAndOptionIdAndOptionDetailIds(productId, optionId, optionDetailIds);
        productOptionRepository.deleteAll(existProductOptionEntities);
    }

    @Override
    public void removeAllOptionOfProduct(Long productId) {
        productOptionRepository.deleteByProductId(productId);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
