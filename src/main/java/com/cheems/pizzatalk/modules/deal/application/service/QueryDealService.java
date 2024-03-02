package com.cheems.pizzatalk.modules.deal.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.filter.StringFilter;
import com.cheems.pizzatalk.modules.deal.application.port.in.query.DealCriteria;
import com.cheems.pizzatalk.modules.deal.application.port.in.share.QueryDealUseCase;
import com.cheems.pizzatalk.modules.deal.application.port.out.QueryDealPort;
import com.cheems.pizzatalk.modules.deal.domain.Deal;
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
public class QueryDealService implements QueryDealUseCase {

    private final QueryDealPort queryDealPort;

    public QueryDealService(QueryDealPort queryDealPort) {
        this.queryDealPort = queryDealPort;
    }

    @Override
    public Optional<Deal> findById(Long id, String... fetchAttributes) {
        DealCriteria criteria = new DealCriteria();

        RangeFilter<Long> idFilter = new RangeFilter<Long>();
        idFilter.setEquals(id);
        criteria.setId(idFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }
        return findByCriteria(criteria);
    }

    @Override
    public Deal getById(Long id, String... fetchAttributes) {
        return findById(id, fetchAttributes).orElseThrow(() -> new BusinessException("Not found deal with id: " + id));
    }

    @Override
    public Optional<Deal> findBySlug(String slug, String... fetchAttributes) {
        DealCriteria criteria = new DealCriteria();

        StringFilter slugFilter = new StringFilter();
        slugFilter.setEquals(slug);
        criteria.setSlug(slugFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }
        return findByCriteria(criteria);
    }

    @Override
    public Deal getBySlug(String slug, String... fetchAttributes) {
        return findBySlug(slug, fetchAttributes).orElseThrow(() -> new BusinessException("Not found deal with slug: " + slug));
    }

    @Override
    public Optional<Deal> findByCriteria(DealCriteria criteria) {
        return queryDealPort.findByCriteria(criteria);
    }

    @Override
    public Deal getByCriteria(DealCriteria criteria) {
        return findByCriteria(criteria).orElseThrow(() -> new BusinessException("Not found deal with criteria: " + criteria));
    }

    @Override
    public List<Deal> findListByCriteria(DealCriteria criteria) {
        return queryDealPort.findListByCriteria(criteria);
    }

    @Override
    public Page<Deal> findPageByCriteria(DealCriteria criteria, Pageable pageable) {
        return queryDealPort.findPageByCriteria(criteria, pageable);
    }
}
