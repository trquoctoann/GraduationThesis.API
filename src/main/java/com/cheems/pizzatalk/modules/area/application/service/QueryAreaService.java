package com.cheems.pizzatalk.modules.area.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.filter.StringFilter;
import com.cheems.pizzatalk.modules.area.application.port.in.query.AreaCriteria;
import com.cheems.pizzatalk.modules.area.application.port.in.share.QueryAreaUseCase;
import com.cheems.pizzatalk.modules.area.application.port.out.QueryAreaPort;
import com.cheems.pizzatalk.modules.area.domain.Area;
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
public class QueryAreaService implements QueryAreaUseCase {

    private final QueryAreaPort queryAreaPort;

    public QueryAreaService(QueryAreaPort queryAreaPort) {
        this.queryAreaPort = queryAreaPort;
    }

    @Override
    public Optional<Area> findById(Long id, String... fetchAttributes) {
        AreaCriteria criteria = new AreaCriteria();

        RangeFilter<Long> idFilter = new RangeFilter<Long>();
        idFilter.setEquals(id);
        criteria.setId(idFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }
        return findByCriteria(criteria);
    }

    @Override
    public Area getById(Long id, String... fetchAttributes) {
        return findById(id, fetchAttributes).orElseThrow(() -> new BusinessException("Not found area with id: " + id));
    }

    @Override
    public Optional<Area> findByCode(String code, String... fetchAttributes) {
        AreaCriteria criteria = new AreaCriteria();

        StringFilter codeFilter = new StringFilter();
        codeFilter.setEquals(code);
        criteria.setCode(codeFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }
        return findByCriteria(criteria);
    }

    @Override
    public Area getByCode(String code, String... fetchAttributes) {
        return findByCode(code, fetchAttributes).orElseThrow(() -> new BusinessException("Not found area with code: " + code));
    }

    @Override
    public Optional<Area> findByCriteria(AreaCriteria criteria) {
        return queryAreaPort.findByCriteria(criteria);
    }

    @Override
    public Area getByCriteria(AreaCriteria criteria) {
        return findByCriteria(criteria).orElseThrow(() -> new BusinessException("Not found area with criteria: " + criteria));
    }

    @Override
    public List<Area> findListByCriteria(AreaCriteria criteria) {
        return queryAreaPort.findListByCriteria(criteria);
    }

    @Override
    public Page<Area> findPageByCriteria(AreaCriteria criteria, Pageable pageable) {
        return queryAreaPort.findPageByCriteria(criteria, pageable);
    }
}
