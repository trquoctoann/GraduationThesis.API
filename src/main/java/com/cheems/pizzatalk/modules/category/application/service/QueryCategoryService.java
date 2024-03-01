package com.cheems.pizzatalk.modules.category.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.filter.StringFilter;
import com.cheems.pizzatalk.modules.category.application.port.in.query.CategoryCriteria;
import com.cheems.pizzatalk.modules.category.application.port.in.share.QueryCategoryUseCase;
import com.cheems.pizzatalk.modules.category.application.port.out.QueryCategoryPort;
import com.cheems.pizzatalk.modules.category.domain.Category;
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
public class QueryCategoryService implements QueryCategoryUseCase {

    private final QueryCategoryPort queryCategoryPort;

    public QueryCategoryService(QueryCategoryPort queryCategoryPort) {
        this.queryCategoryPort = queryCategoryPort;
    }

    @Override
    public Optional<Category> findById(Long id, String... fetchAttributes) {
        CategoryCriteria criteria = new CategoryCriteria();

        RangeFilter<Long> idFilter = new RangeFilter<Long>();
        idFilter.setEquals(id);
        criteria.setId(idFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }
        return findByCriteria(criteria);
    }

    @Override
    public Category getById(Long id, String... fetchAttributes) {
        return findById(id, fetchAttributes).orElseThrow(() -> new BusinessException("Not found category with id: " + id));
    }

    @Override
    public Optional<Category> findByName(String name, String... fetchAttributes) {
        CategoryCriteria criteria = new CategoryCriteria();

        StringFilter nameFilter = new StringFilter();
        nameFilter.setEquals(name);
        criteria.setName(nameFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }
        return findByCriteria(criteria);
    }

    @Override
    public Category getByName(String name, String... fetchAttributes) {
        return findByName(name, fetchAttributes).orElseThrow(() -> new BusinessException("Not found category with name: " + name));
    }

    @Override
    public Optional<Category> findByCriteria(CategoryCriteria criteria) {
        return queryCategoryPort.findByCriteria(criteria);
    }

    @Override
    public Category getByCriteria(CategoryCriteria criteria) {
        return findByCriteria(criteria).orElseThrow(() -> new BusinessException("Not found category with criteria: " + criteria));
    }

    @Override
    public List<Category> findListByCriteria(CategoryCriteria criteria) {
        return queryCategoryPort.findListByCriteria(criteria);
    }

    @Override
    public Page<Category> findPageByCriteria(CategoryCriteria criteria, Pageable pageable) {
        return queryCategoryPort.findPageByCriteria(criteria, pageable);
    }
}
