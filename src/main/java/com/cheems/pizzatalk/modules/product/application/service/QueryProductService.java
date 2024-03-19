package com.cheems.pizzatalk.modules.product.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.filter.StringFilter;
import com.cheems.pizzatalk.modules.product.application.port.in.query.ProductCriteria;
import com.cheems.pizzatalk.modules.product.application.port.in.share.QueryProductUseCase;
import com.cheems.pizzatalk.modules.product.application.port.out.QueryProductPort;
import com.cheems.pizzatalk.modules.product.domain.Product;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class QueryProductService implements QueryProductUseCase {

    private final QueryProductPort queryProductPort;

    public QueryProductService(QueryProductPort queryProductPort) {
        this.queryProductPort = queryProductPort;
    }

    @Override
    public Optional<Product> findById(Long id, String... fetchAttributes) {
        ProductCriteria criteria = new ProductCriteria();

        RangeFilter<Long> idFilter = new RangeFilter<>();
        idFilter.setEquals(id);
        criteria.setId(idFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }

        return findByCriteria(criteria);
    }

    @Override
    public Product getById(Long id, String... fetchAttributes) {
        return findById(id, fetchAttributes).orElseThrow(() -> new BusinessException("Not found product with id: " + id));
    }

    @Override
    public Optional<Product> findBySku(String sku, String... fetchAttributes) {
        ProductCriteria criteria = new ProductCriteria();

        StringFilter skuFilter = new StringFilter();
        skuFilter.setEquals(sku);
        criteria.setSku(skuFilter);
        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }

        return findByCriteria(criteria);
    }

    @Override
    public Product getBySku(String sku, String... fetchAttributes) {
        return findBySku(sku, fetchAttributes).orElseThrow(() -> new BusinessException("Not found product with sku: " + sku));
    }

    @Override
    public Optional<Product> findByCriteria(ProductCriteria criteria) {
        return queryProductPort.findByCriteria(criteria);
    }

    @Override
    public Product getByCriteria(ProductCriteria criteria) {
        return findByCriteria(criteria).orElseThrow(() -> new BusinessException("Not found product with criteria" + criteria.toString()));
    }

    @Override
    public List<Product> findListByCriteria(ProductCriteria criteria) {
        return queryProductPort.findListByCriteria(criteria);
    }

    @Override
    public Page<Product> findPageByCriteria(ProductCriteria criteria, Pageable pageable) {
        return queryProductPort.findPageByCriteria(criteria, pageable);
    }

    @Override
    public List<Product> findProductsByCategoryId(Long categoryId) {
        ProductCriteria criteria = new ProductCriteria();

        RangeFilter<Long> categoryIdFilter = new RangeFilter<>();
        categoryIdFilter.setEquals(categoryId);

        criteria.setCategoryId(categoryIdFilter);
        return queryProductPort.findListByCriteria(criteria);
    }
}
