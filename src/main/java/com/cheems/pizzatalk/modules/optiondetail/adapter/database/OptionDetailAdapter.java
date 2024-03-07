package com.cheems.pizzatalk.modules.optiondetail.adapter.database;

import com.cheems.pizzatalk.entities.mapper.OptionDetailMapper;
import com.cheems.pizzatalk.modules.optiondetail.application.port.out.OptionDetailPort;
import com.cheems.pizzatalk.modules.optiondetail.domain.OptionDetail;
import com.cheems.pizzatalk.repository.OptionDetailRepository;
import org.springframework.stereotype.Component;

@Component
public class OptionDetailAdapter implements OptionDetailPort {

    private final OptionDetailRepository optionDetailRepository;

    private final OptionDetailMapper optionDetailMapper;

    public OptionDetailAdapter(OptionDetailRepository optionDetailRepository, OptionDetailMapper optionDetailMapper) {
        this.optionDetailRepository = optionDetailRepository;
        this.optionDetailMapper = optionDetailMapper;
    }

    @Override
    public OptionDetail save(OptionDetail optionDetail) {
        return optionDetailMapper.toDomain(optionDetailRepository.save(optionDetailMapper.toEntity(optionDetail)));
    }

    @Override
    public void deleteById(Long id) {
        optionDetailRepository.deleteById(id);
    }
}
