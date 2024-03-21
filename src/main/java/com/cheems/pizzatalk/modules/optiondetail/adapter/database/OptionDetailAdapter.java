package com.cheems.pizzatalk.modules.optiondetail.adapter.database;

import com.cheems.pizzatalk.entities.mapper.OptionDetailMapper;
import com.cheems.pizzatalk.modules.optiondetail.application.port.out.OptionDetailPort;
import com.cheems.pizzatalk.modules.optiondetail.domain.OptionDetail;
import com.cheems.pizzatalk.repository.OptionDetailRepository;
import com.cheems.pizzatalk.repository.ProductOptionRepository;
import org.springframework.stereotype.Component;

@Component
public class OptionDetailAdapter implements OptionDetailPort {

    private final OptionDetailRepository optionDetailRepository;

    private final ProductOptionRepository productOptionRepository;

    private final OptionDetailMapper optionDetailMapper;

    public OptionDetailAdapter(
        OptionDetailRepository optionDetailRepository,
        ProductOptionRepository productOptionRepository,
        OptionDetailMapper optionDetailMapper
    ) {
        this.optionDetailRepository = optionDetailRepository;
        this.productOptionRepository = productOptionRepository;
        this.optionDetailMapper = optionDetailMapper;
    }

    @Override
    public OptionDetail save(OptionDetail optionDetail) {
        return optionDetailMapper.toDomain(optionDetailRepository.save(optionDetailMapper.toEntity(optionDetail)));
    }

    @Override
    public void removeAllProductOfOptionDetail(Long optionDetailId) {
        productOptionRepository.deleteByOptionDetailId(optionDetailId);
    }

    @Override
    public void deleteById(Long id) {
        optionDetailRepository.deleteById(id);
    }
}
