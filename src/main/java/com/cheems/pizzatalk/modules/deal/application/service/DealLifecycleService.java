package com.cheems.pizzatalk.modules.deal.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.entities.mapper.DealMapper;
import com.cheems.pizzatalk.modules.deal.application.port.in.command.CreateDealCommand;
import com.cheems.pizzatalk.modules.deal.application.port.in.command.UpdateDealCommand;
import com.cheems.pizzatalk.modules.deal.application.port.in.share.DealLifecycleUseCase;
import com.cheems.pizzatalk.modules.deal.application.port.in.share.QueryDealUseCase;
import com.cheems.pizzatalk.modules.deal.application.port.out.DealPort;
import com.cheems.pizzatalk.modules.deal.domain.Deal;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DealLifecycleService implements DealLifecycleUseCase {

    private static final Logger log = LoggerFactory.getLogger(DealLifecycleService.class);

    private final ObjectMapper objectMapper;

    private final DealPort dealPort;

    private final QueryDealUseCase queryDealUseCase;

    public DealLifecycleService(ObjectMapper objectMapper, DealPort dealPort, QueryDealUseCase queryDealUseCase) {
        this.objectMapper = objectMapper;
        this.dealPort = dealPort;
        this.queryDealUseCase = queryDealUseCase;
    }

    @Override
    public Deal create(CreateDealCommand command) {
        log.debug("Creating deal: {}", command);
        if (queryDealUseCase.findBySlug(command.getSlug()).isPresent()) {
            throw new BusinessException("Deal already exists");
        }

        Deal deal = objectMapper.convertValue(command, Deal.class);

        if (command.getParentDealId() != null) {
            Optional<Deal> parentDeal = queryDealUseCase.findById(command.getParentDealId());
            if (!parentDeal.isPresent()) {
                throw new BusinessException("Not found parent deal with id: " + command.getParentDealId());
            }
        }
        deal = dealPort.save(deal);

        log.debug("Created deal: {}", command);
        return deal;
    }

    @Override
    public Deal update(UpdateDealCommand command) {
        log.debug("Updating deal, id: {}", command.getId());
        Deal existDeal = queryDealUseCase.getById(command.getId());

        Deal deal = objectMapper.convertValue(command, Deal.class);
        deal.setId(existDeal.getId());
        deal.setSlug(existDeal.getSlug());

        if (command.getParentDealId() != null) {
            Optional<Deal> parentDeal = queryDealUseCase.findById(command.getParentDealId());
            if (!parentDeal.isPresent()) {
                throw new BusinessException("Not found parent deal with id: " + command.getParentDealId());
            }
        }
        deal = dealPort.save(deal);

        log.debug("Updated deal, id: {}", command.getId());
        return deal;
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Deleting deal, id: {}", id);
        Deal deletingDeal = queryDealUseCase.getById(id, DealMapper.DOMAIN_DEAL_VARIATION);
        Set<Deal> deletingDealVariations = deletingDeal.getDealVariations();

        if (deletingDealVariations != null && deletingDealVariations.size() > 0) {
            for (Deal dealVariation : deletingDealVariations) {
                dealPort.deleteById(dealVariation.getId());
            }
        }
        dealPort.deleteById(id);
        log.debug("Deleted deal, id: {}", id);
    }
}
