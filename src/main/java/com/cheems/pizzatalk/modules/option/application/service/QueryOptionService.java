package com.cheems.pizzatalk.modules.option.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.common.filter.RangeFilter;
import com.cheems.pizzatalk.common.filter.StringFilter;
import com.cheems.pizzatalk.modules.option.application.port.in.query.OptionCriteria;
import com.cheems.pizzatalk.modules.option.application.port.in.share.QueryOptionUseCase;
import com.cheems.pizzatalk.modules.option.application.port.out.QueryOptionPort;
import com.cheems.pizzatalk.modules.option.domain.Option;
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
public class QueryOptionService implements QueryOptionUseCase {

    private final QueryOptionPort queryOptionPort;

    public QueryOptionService(QueryOptionPort queryOptionPort) {
        this.queryOptionPort = queryOptionPort;
    }

    @Override
    public Optional<Option> findById(Long id, String... fetchAttributes) {
        OptionCriteria criteria = new OptionCriteria();

        RangeFilter<Long> idFilter = new RangeFilter<>();
        idFilter.setEquals(id);
        criteria.setId(idFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }

        return findByCriteria(criteria);
    }

    @Override
    public Option getById(Long id, String... fetchAttributes) {
        return findById(id, fetchAttributes).orElseThrow(() -> new BusinessException("Not found option with id: " + id));
    }

    @Override
    public Optional<Option> findByCode(String code, String... fetchAttributes) {
        OptionCriteria criteria = new OptionCriteria();

        StringFilter codeFilter = new StringFilter();
        codeFilter.setEquals(code);
        criteria.setCode(codeFilter);

        if (fetchAttributes != null) {
            criteria.setFetchAttributes(Arrays.stream(fetchAttributes).collect(Collectors.toSet()));
        }

        return findByCriteria(criteria);
    }

    @Override
    public Option getByCode(String code, String... fetchAttributes) {
        return findByCode(code, fetchAttributes).orElseThrow(() -> new BusinessException("Not found option with code: " + code));
    }

    @Override
    public Optional<Option> findByCriteria(OptionCriteria criteria) {
        return queryOptionPort.findByCriteria(criteria);
    }

    @Override
    public Option getByCriteria(OptionCriteria criteria) {
        return findByCriteria(criteria).orElseThrow(() -> new BusinessException("Not found option with criteria" + criteria.toString()));
    }

    @Override
    public List<Option> findListByCriteria(OptionCriteria criteria) {
        return queryOptionPort.findListByCriteria(criteria);
    }

    @Override
    public Page<Option> findPageByCriteria(OptionCriteria criteria, Pageable pageable) {
        return queryOptionPort.findPageByCriteria(criteria, pageable);
    }
}
