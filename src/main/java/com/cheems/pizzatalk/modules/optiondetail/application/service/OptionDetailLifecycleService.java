package com.cheems.pizzatalk.modules.optiondetail.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.entities.enumeration.CommerceStatus;
import com.cheems.pizzatalk.modules.option.application.port.in.share.QueryOptionUseCase;
import com.cheems.pizzatalk.modules.option.domain.Option;
import com.cheems.pizzatalk.modules.optiondetail.application.port.in.command.CreateOptionDetailCommand;
import com.cheems.pizzatalk.modules.optiondetail.application.port.in.command.UpdateOptionDetailCommand;
import com.cheems.pizzatalk.modules.optiondetail.application.port.in.share.OptionDetailLifecycleUseCase;
import com.cheems.pizzatalk.modules.optiondetail.application.port.in.share.OptionDetailProductUseCase;
import com.cheems.pizzatalk.modules.optiondetail.application.port.in.share.QueryOptionDetailUseCase;
import com.cheems.pizzatalk.modules.optiondetail.application.port.out.OptionDetailPort;
import com.cheems.pizzatalk.modules.optiondetail.domain.OptionDetail;
import com.cheems.pizzatalk.modules.stockitem.application.port.in.share.StockItemLifecycleUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OptionDetailLifecycleService implements OptionDetailLifecycleUseCase {

    private static final Logger log = LoggerFactory.getLogger(OptionDetailLifecycleService.class);

    private final ObjectMapper objectMapper;

    private final OptionDetailPort optionDetailPort;

    private final OptionDetailProductUseCase optionDetailProductUseCase;

    private final QueryOptionDetailUseCase queryOptionDetailUseCase;

    private final StockItemLifecycleUseCase stockItemLifecycleUseCase;

    private final QueryOptionUseCase queryOptionUseCase;

    public OptionDetailLifecycleService(
        ObjectMapper objectMapper,
        OptionDetailPort optionDetailPort,
        OptionDetailProductUseCase optionDetailProductUseCase,
        StockItemLifecycleUseCase stockItemLifecycleUseCase,
        QueryOptionDetailUseCase queryOptionDetailUseCase,
        QueryOptionUseCase queryOptionUseCase
    ) {
        this.objectMapper = objectMapper;
        this.optionDetailPort = optionDetailPort;
        this.optionDetailProductUseCase = optionDetailProductUseCase;
        this.stockItemLifecycleUseCase = stockItemLifecycleUseCase;
        this.queryOptionDetailUseCase = queryOptionDetailUseCase;
        this.queryOptionUseCase = queryOptionUseCase;
    }

    @Override
    public OptionDetail create(CreateOptionDetailCommand command) {
        log.debug("Creating option detail: {}", command);
        if (queryOptionDetailUseCase.findBySku(command.getSku()).isPresent()) {
            throw new BusinessException("Option Detail SKU already exists");
        }
        validateOptionDetail(command);

        OptionDetail optionDetail = objectMapper.convertValue(command, OptionDetail.class);
        optionDetail = optionDetailPort.save(optionDetail);

        log.debug("Created option detail: {}", command);
        return optionDetail;
    }

    @Override
    public OptionDetail update(UpdateOptionDetailCommand command) {
        log.debug("Updating option detail, id: {}", command.getId());
        OptionDetail existOptionDetail = queryOptionDetailUseCase.getById(command.getId());

        validateOptionDetail(command);

        OptionDetail optionDetail = objectMapper.convertValue(command, OptionDetail.class);
        optionDetail.setId(existOptionDetail.getId());
        optionDetail.setSku(existOptionDetail.getSku());
        optionDetail.setStatus(existOptionDetail.getStatus());

        optionDetail = optionDetailPort.save(optionDetail);
        log.debug("Updated option detail, id: {}", command.getId());
        return optionDetail;
    }

    @Override
    public OptionDetail updateCommerceStatus(Long id, CommerceStatus newStatus) {
        log.debug("Updating commerce status of option detail, id: {}", id);
        OptionDetail existOptionDetail = queryOptionDetailUseCase.getById(id);
        CommerceStatus oldStatus = existOptionDetail.getStatus();

        if (
            newStatus.equals(CommerceStatus.UPCOMING) ||
            oldStatus.equals(CommerceStatus.DISCONTINUED) ||
            (oldStatus.equals(CommerceStatus.UPCOMING) && newStatus.equals(CommerceStatus.DISCONTINUED)) ||
            (oldStatus.equals(CommerceStatus.UPCOMING) && newStatus.equals(CommerceStatus.INACTIVE))
        ) {
            throw new BusinessException("Cannot change status from " + oldStatus + " to " + newStatus);
        }

        if (newStatus.equals(CommerceStatus.DISCONTINUED)) {
            stockItemLifecycleUseCase.removeAllStoreOfOptionDetail(id);
            optionDetailProductUseCase.removeAllProductOfOptionDetail(id);
        }

        existOptionDetail.setStatus(newStatus);
        existOptionDetail = optionDetailPort.save(existOptionDetail);

        log.debug("Updated commerce status of option detail, id: {}", id);
        return existOptionDetail;
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Deleting option detail, id: {}", id);
        optionDetailProductUseCase.removeAllProductOfOptionDetail(id);
        optionDetailPort.deleteById(id);
        log.debug("Deleted option detail, id: {}", id);
    }

    private <T extends CreateOptionDetailCommand> void validateOptionDetail(T command) {
        if (command.getOptionId() != null) {
            Optional<Option> option = queryOptionUseCase.findById(command.getOptionId());
            if (!option.isPresent()) {
                throw new BusinessException("Not found option with id: " + command.getOptionId());
            }
        }
    }
}
