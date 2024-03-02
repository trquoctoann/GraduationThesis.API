package com.cheems.pizzatalk.modules.deal.adapter.database;

import com.cheems.pizzatalk.entities.mapper.DealMapper;
import com.cheems.pizzatalk.modules.deal.application.port.out.DealPort;
import com.cheems.pizzatalk.modules.deal.domain.Deal;
import com.cheems.pizzatalk.repository.DealRepository;
import org.springframework.stereotype.Component;

@Component
public class DealAdapter implements DealPort {

    private final DealRepository dealRepository;

    private final DealMapper dealMapper;

    public DealAdapter(DealRepository dealRepository, DealMapper dealMapper) {
        this.dealRepository = dealRepository;
        this.dealMapper = dealMapper;
    }

    @Override
    public Deal save(Deal deal) {
        return dealMapper.toDomain(dealRepository.save(dealMapper.toEntity(deal)));
    }

    @Override
    public void deleteById(Long id) {
        dealRepository.deleteById(id);
    }
}
