package com.cheems.pizzatalk.modules.option.adapter.database;

import com.cheems.pizzatalk.entities.mapper.OptionMapper;
import com.cheems.pizzatalk.modules.option.application.port.out.OptionPort;
import com.cheems.pizzatalk.modules.option.domain.Option;
import com.cheems.pizzatalk.repository.OptionRepository;
import com.cheems.pizzatalk.repository.ProductOptionRepository;
import org.springframework.stereotype.Component;

@Component
public class OptionAdapter implements OptionPort {

    private final OptionRepository optionRepository;

    private final ProductOptionRepository productOptionRepository;

    private final OptionMapper optionMapper;

    public OptionAdapter(OptionRepository optionRepository, ProductOptionRepository productOptionRepository, OptionMapper optionMapper) {
        this.optionRepository = optionRepository;
        this.productOptionRepository = productOptionRepository;
        this.optionMapper = optionMapper;
    }

    @Override
    public Option save(Option option) {
        return optionMapper.toDomain(optionRepository.save(optionMapper.toEntity(option)));
    }

    @Override
    public void removeAllProductOfOption(Long optionId) {
        productOptionRepository.deleteByOptionId(optionId);
    }

    @Override
    public void deleteById(Long id) {
        optionRepository.deleteById(id);
    }
}
