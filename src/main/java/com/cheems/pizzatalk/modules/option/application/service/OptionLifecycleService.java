package com.cheems.pizzatalk.modules.option.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.entities.mapper.OptionMapper;
import com.cheems.pizzatalk.modules.option.application.port.in.command.CreateOptionCommand;
import com.cheems.pizzatalk.modules.option.application.port.in.command.UpdateOptionCommand;
import com.cheems.pizzatalk.modules.option.application.port.in.share.OptionLifecycleUseCase;
import com.cheems.pizzatalk.modules.option.application.port.in.share.OptionProductUseCase;
import com.cheems.pizzatalk.modules.option.application.port.in.share.QueryOptionUseCase;
import com.cheems.pizzatalk.modules.option.application.port.out.OptionPort;
import com.cheems.pizzatalk.modules.option.domain.Option;
import com.cheems.pizzatalk.modules.optiondetail.application.port.in.share.OptionDetailLifecycleUseCase;
import com.cheems.pizzatalk.modules.optiondetail.domain.OptionDetail;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
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

    private final OptionDetailLifecycleUseCase optionDetailLifecycleUseCase;

    private final OptionProductUseCase optionProductUseCase;

    private final QueryOptionUseCase queryOptionUseCase;

    public OptionLifecycleService(
        ObjectMapper objectMapper,
        OptionPort optionPort,
        OptionDetailLifecycleUseCase optionDetailLifecycleUseCase,
        OptionProductUseCase optionProductUseCase,
        QueryOptionUseCase queryOptionUseCase
    ) {
        this.objectMapper = objectMapper;
        this.optionPort = optionPort;
        this.optionDetailLifecycleUseCase = optionDetailLifecycleUseCase;
        this.optionProductUseCase = optionProductUseCase;
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
        log.debug("Deleting option, id: {}", id);
        Option existOption = queryOptionUseCase.getById(id, OptionMapper.DOMAIN_OPTION_DETAILS);
        Set<OptionDetail> linkedOptionDetails = existOption.getOptionDetails();

        linkedOptionDetails.forEach(linkedOptionDetail -> optionDetailLifecycleUseCase.deleteById(linkedOptionDetail.getId()));
        optionProductUseCase.removeAllProductOfOption(id);
        optionPort.deleteById(id);
        log.debug("Deleting option, id: {}", id);
    }
}
