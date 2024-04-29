package com.cheems.pizzatalk.modules.optiondetail.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.filter.StringFilter;
import com.cheems.pizzatalk.modules.optiondetail.application.port.in.query.OptionDetailCriteria;
import com.cheems.pizzatalk.modules.optiondetail.application.port.in.share.QueryOptionDetailUseCase;
import com.cheems.pizzatalk.modules.optiondetail.application.port.out.QueryOptionDetailPort;
import com.cheems.pizzatalk.modules.optiondetail.domain.OptionDetail;
import java.util.ArrayList;
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
public class QueryOptionDetailService implements QueryOptionDetailUseCase {

    private final QueryOptionDetailPort queryOptionDetailPort;

    public QueryOptionDetailService(QueryOptionDetailPort queryOptionDetailPort) {
        this.queryOptionDetailPort = queryOptionDetailPort;
    }

    @Override
    public Optional<OptionDetail> findById(Long id, String... fetchAttributes) {
        OptionDetailCriteria criteria = new OptionDetailCriteria();

        RangeFilter<Long> idFilter = new RangeFilter<>();
        idFilter.setEquals(id);
        criteria.setId(idFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }

        return findByCriteria(criteria);
    }

    @Override
    public OptionDetail getById(Long id, String... fetchAttributes) {
        return findById(id, fetchAttributes).orElseThrow(() -> new BusinessException("Not found option detail with id: " + id));
    }

    @Override
    public Optional<OptionDetail> findBySku(String sku, String... fetchAttributes) {
        OptionDetailCriteria criteria = new OptionDetailCriteria();

        StringFilter skuFilter = new StringFilter();
        skuFilter.setEquals(sku);
        criteria.setSku(skuFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }

        return findByCriteria(criteria);
    }

    @Override
    public OptionDetail getBySku(String sku, String... fetchAttributes) {
        return findBySku(sku, fetchAttributes).orElseThrow(() -> new BusinessException("Not found option detail with SKU: " + sku));
    }

    @Override
    public Optional<OptionDetail> findByCriteria(OptionDetailCriteria criteria) {
        return queryOptionDetailPort.findByCriteria(criteria);
    }

    @Override
    public OptionDetail getByCriteria(OptionDetailCriteria criteria) {
        return findByCriteria(criteria)
            .orElseThrow(() -> new BusinessException("Not found option detail with criteria" + criteria.toString()));
    }

    @Override
    public List<OptionDetail> findListByCriteria(OptionDetailCriteria criteria) {
        return queryOptionDetailPort.findListByCriteria(criteria);
    }

    @Override
    public Page<OptionDetail> findPageByCriteria(OptionDetailCriteria criteria, Pageable pageable) {
        return queryOptionDetailPort.findPageByCriteria(criteria, pageable);
    }

    @Override
    public List<OptionDetail> findListByListIds(List<Long> ids) {
        OptionDetailCriteria criteria = new OptionDetailCriteria();

        RangeFilter<Long> idFilter = new RangeFilter<>();
        idFilter.setIn(new ArrayList<>(ids));
        criteria.setId(idFilter);

        return findListByCriteria(criteria);
    }

    @Override
    public List<OptionDetail> findListByProductIdAndOptionId(Long productId, Long optionId) {
        OptionDetailCriteria criteria = new OptionDetailCriteria();

        RangeFilter<Long> productIdFilter = new RangeFilter<>();
        productIdFilter.setEquals(productId);

        RangeFilter<Long> optionIdFilter = new RangeFilter<>();
        optionIdFilter.setEquals(optionId);

        criteria.setProductId(productIdFilter);
        criteria.setOptionId(optionIdFilter);

        return findListByCriteria(criteria);
    }

    @Override
    public List<OptionDetail> findListByCartItemId(Long cartItemId) {
        OptionDetailCriteria criteria = new OptionDetailCriteria();

        RangeFilter<Long> cartItemIdFilter = new RangeFilter<>();
        cartItemIdFilter.setEquals(cartItemId);
        criteria.setCartItemId(cartItemIdFilter);

        return findListByCriteria(criteria);
    }
}
