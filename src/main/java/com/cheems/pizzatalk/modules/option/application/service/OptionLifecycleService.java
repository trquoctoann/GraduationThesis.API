package com.cheems.pizzatalk.modules.option.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.modules.option.application.port.in.command.CreateOptionCommand;
import com.cheems.pizzatalk.modules.option.application.port.in.command.UpdateOptionCommand;
import com.cheems.pizzatalk.modules.option.application.port.in.share.OptionLifecycleUseCase;
import com.cheems.pizzatalk.modules.option.application.port.in.share.QueryOptionUseCase;
import com.cheems.pizzatalk.modules.option.application.port.out.OptionPort;
import com.cheems.pizzatalk.modules.option.domain.Option;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OptionLifecycleService implements OptionLifecycleUseCase {

    private static final Logger log = LoggerFactory.getLogger(OptionLifecycleService.class);

    private final ObjectMapper objectMapper;

    private final OptionPort optionPort;

    private final QueryOptionUseCase queryOptionUseCase;

    public OptionLifecycleService(ObjectMapper objectMapper, OptionPort optionPort, QueryOptionUseCase queryOptionUseCase) {
        this.objectMapper = objectMapper;
        this.optionPort = optionPort;
        this.queryOptionUseCase = queryOptionUseCase;
    }

    @Override
    public Option create(CreateOptionCommand command) {
        log.debug("Creating option: {}", command);
        if (queryOptionUseCase.findByCode(command.getCode()).isPresent()) {
            throw new BusinessException("Option code already exists");
        }

        Option option = objectMapper.convertValue(command, Option.class);
        option = optionPort.save(option);

        log.debug("Created option: {}", command);
        return option;
    }

    @Override
    public Option update(UpdateOptionCommand command) {
        log.debug("Updating option, id: {}", command.getId());
        Option existOption = queryOptionUseCase.getById(command.getId());

        Option option = objectMapper.convertValue(command, Option.class);
        option.setId(existOption.getId());
        option.setCode(existOption.getCode());
        option.setStatus(existOption.getStatus());

        option = optionPort.save(option);
        log.debug("Updated option, id: {}", command.getId());
        return option;
    }

    @Override
    public void deleteById(Long id) {
        optionPort.deleteById(id);
    }
}
