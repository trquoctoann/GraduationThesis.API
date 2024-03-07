package com.cheems.pizzatalk.modules.optiondetail.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.modules.option.application.port.in.share.QueryOptionUseCase;
import com.cheems.pizzatalk.modules.option.domain.Option;
import com.cheems.pizzatalk.modules.optiondetail.application.port.in.command.CreateOptionDetailCommand;
import com.cheems.pizzatalk.modules.optiondetail.application.port.in.command.UpdateOptionDetailCommand;
import com.cheems.pizzatalk.modules.optiondetail.application.port.in.share.OptionDetailLifecycleUseCase;
import com.cheems.pizzatalk.modules.optiondetail.application.port.in.share.QueryOptionDetailUseCase;
import com.cheems.pizzatalk.modules.optiondetail.application.port.out.OptionDetailPort;
import com.cheems.pizzatalk.modules.optiondetail.domain.OptionDetail;
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

    private final QueryOptionDetailUseCase queryOptionDetailUseCase;

    private final QueryOptionUseCase queryOptionUseCase;

    public OptionDetailLifecycleService(
        ObjectMapper objectMapper,
        OptionDetailPort optionDetailPort,
        QueryOptionDetailUseCase queryOptionDetailUseCase,
        QueryOptionUseCase queryOptionUseCase
    ) {
        this.objectMapper = objectMapper;
        this.optionDetailPort = optionDetailPort;
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
    public void deleteById(Long id) {
        log.debug("Deleting option detail, id: {}", id);
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
