package com.cheems.pizzatalk.modules.option.adapter.database;

import com.cheems.pizzatalk.entities.mapper.OptionMapper;
import com.cheems.pizzatalk.modules.option.application.port.out.OptionPort;
import com.cheems.pizzatalk.modules.option.domain.Option;
import com.cheems.pizzatalk.repository.OptionRepository;
import org.springframework.stereotype.Component;

@Component
public class OptionAdapter implements OptionPort {

    private final OptionRepository optionRepository;

    private final OptionMapper optionMapper;

    public OptionAdapter(OptionRepository optionRepository, OptionMapper optionMapper) {
        this.optionRepository = optionRepository;
        this.optionMapper = optionMapper;
    }

    @Override
    public Option save(Option option) {
        return optionMapper.toDomain(optionRepository.save(optionMapper.toEntity(option)));
    }

    @Override
    public void deleteById(Long id) {
        optionRepository.deleteById(id);
    }
}
