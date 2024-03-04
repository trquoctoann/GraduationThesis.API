package com.cheems.pizzatalk.modules.product.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.entities.enumeration.CommerceStatus;
import com.cheems.pizzatalk.entities.mapper.ProductMapper;
import com.cheems.pizzatalk.modules.category.application.port.in.share.QueryCategoryUseCase;
import com.cheems.pizzatalk.modules.category.domain.Category;
import com.cheems.pizzatalk.modules.product.application.port.in.command.CreateProductCommand;
import com.cheems.pizzatalk.modules.product.application.port.in.command.UpdateProductCommand;
import com.cheems.pizzatalk.modules.product.application.port.in.command.UpdateProductStatusCommand;
import com.cheems.pizzatalk.modules.product.application.port.in.share.ProductLifecycleUseCase;
import com.cheems.pizzatalk.modules.product.application.port.in.share.QueryProductUseCase;
import com.cheems.pizzatalk.modules.product.application.port.out.ProductPort;
import com.cheems.pizzatalk.modules.product.domain.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductLifecycleService implements ProductLifecycleUseCase {

    private static final Logger log = LoggerFactory.getLogger(ProductLifecycleService.class);

    private final ObjectMapper objectMapper;

    private final ProductPort productPort;

    private final QueryProductUseCase queryProductUseCase;

    private final QueryCategoryUseCase queryCategoryUseCase;

    public ProductLifecycleService(
        ObjectMapper objectMapper,
        ProductPort productPort,
        QueryProductUseCase queryProductUseCase,
        QueryCategoryUseCase queryCategoryUseCase
    ) {
        this.objectMapper = objectMapper;
        this.productPort = productPort;
        this.queryProductUseCase = queryProductUseCase;
        this.queryCategoryUseCase = queryCategoryUseCase;
    }

    @Override
    public Product create(CreateProductCommand command) {
        log.debug("Creating product: {}", command);
        if (queryProductUseCase.findBySku(command.getSku()).isPresent()) {
            throw new BusinessException("Product SKU already exists");
        }
        validateProduct(command);

        Product product = objectMapper.convertValue(command, Product.class);
        product = productPort.save(product);
        log.debug("Created product: {}", command);
        return product;
    }

    @Override
    public Product update(UpdateProductCommand command) {
        log.debug("Updating product, id: {}", command.getId());
        Product existProduct = queryProductUseCase.getById(command.getId());

        validateProduct(command);

        Product product = objectMapper.convertValue(command, Product.class);
        product.setId(existProduct.getId());
        product.setSlug(existProduct.getSlug());
        product.setSku(existProduct.getSku());
        product.setStatus(existProduct.getStatus());

        product = productPort.save(product);
        log.debug("Updated product, id: {}", command.getId());
        return product;
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Deleting product, id: {}", id);
        Product deletingProduct = queryProductUseCase.getById(id, ProductMapper.DOMAIN_PRODUCT_VARIATIONS);
        Set<Product> deletingProductVariations = deletingProduct.getProductVariations();

        if (deletingProductVariations != null && deletingProductVariations.size() > 0) {
            for (Product productVariation : deletingProductVariations) {
                productPort.deleteById(productVariation.getId());
            }
        }
        productPort.deleteById(id);
        log.debug("Deleted product, id: {}", id);
    }

    @Override
    public Product updateCommerceStatus(UpdateProductStatusCommand command) {
        log.debug("Updating commerce status of product, id: {}", command.getId());
        Product existProduct = queryProductUseCase.getById(command.getId(), ProductMapper.DOMAIN_PRODUCT_VARIATIONS);

        if (
            command.getStatus().equals(CommerceStatus.UPCOMING) ||
            existProduct.getStatus().equals(CommerceStatus.DISCONTINUED) ||
            (existProduct.getStatus().equals(CommerceStatus.UPCOMING) && command.getStatus().equals(CommerceStatus.DISCONTINUED)) ||
            (existProduct.getStatus().equals(CommerceStatus.UPCOMING) && command.getStatus().equals(CommerceStatus.INACTIVE))
        ) {
            throw new BusinessException("Cannot change status from " + existProduct.getStatus() + " to " + command.getStatus());
        }

        if (command.getStatus().equals(CommerceStatus.DISCONTINUED)) {
            Set<Product> existProductVariations = existProduct.getProductVariations();
            if (existProductVariations != null && existProductVariations.size() > 0) {
                for (Product productVariation : existProductVariations) {
                    productVariation.setStatus(command.getStatus());
                    productPort.save(productVariation);
                }
            }
        }

        existProduct.setStatus(command.getStatus());
        existProduct = productPort.save(existProduct);
        log.debug(
            "Updated commerce status of product, id: {} from {} to {}",
            command.getId(),
            existProduct.getStatus(),
            command.getStatus()
        );
        return existProduct;
    }

    private <T extends CreateProductCommand> void validateProduct(T command) {
        if (command.getParentProductId() != null) {
            Optional<Product> parentProduct = queryProductUseCase.findById(command.getParentProductId());
            if (!parentProduct.isPresent()) {
                throw new BusinessException("Not found parent product with id: " + command.getParentProductId());
            }
        }
        if (command.getCategoryId() != null) {
            Optional<Category> category = queryCategoryUseCase.findById(command.getCategoryId());
            if (!category.isPresent()) {
                throw new BusinessException("Not found product's category with id: " + command.getCategoryId());
            }
        }
    }
}
