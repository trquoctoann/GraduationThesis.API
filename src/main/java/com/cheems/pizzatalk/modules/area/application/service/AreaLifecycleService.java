package com.cheems.pizzatalk.modules.area.application.service;

import com.cheems.pizzatalk.common.exception.BusinessException;
import com.cheems.pizzatalk.modules.area.application.port.in.command.CreateAreaCommand;
import com.cheems.pizzatalk.modules.area.application.port.in.command.UpdateAreaCommand;
import com.cheems.pizzatalk.modules.area.application.port.in.share.AreaLifecycleUseCase;
import com.cheems.pizzatalk.modules.area.application.port.in.share.QueryAreaUseCase;
import com.cheems.pizzatalk.modules.area.application.port.out.AreaPort;
import com.cheems.pizzatalk.modules.area.domain.Area;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AreaLifecycleService implements AreaLifecycleUseCase {

    private static final Logger log = LoggerFactory.getLogger(AreaLifecycleService.class);

    private final ObjectMapper objectMapper;

    private final AreaPort areaPort;

    private final QueryAreaUseCase queryAreaUseCase;

    public AreaLifecycleService(ObjectMapper objectMapper, AreaPort areaPort, QueryAreaUseCase queryAreaUseCase) {
        this.objectMapper = objectMapper;
        this.areaPort = areaPort;
        this.queryAreaUseCase = queryAreaUseCase;
    }

    @Override
    public Area create(CreateAreaCommand command) {
        log.debug("Creating area: {}", command);
        if (queryAreaUseCase.findByCode(command.getCode()).isPresent()) {
            throw new BusinessException("Area code already exists");
        }

        Area area = objectMapper.convertValue(command, Area.class);
        area = areaPort.save(area);

        log.debug("Created area: {}", command);
        return area;
    }

    @Override
    public Area update(UpdateAreaCommand command) {
        log.debug("Updating area, id: {}", command.getId());
        Area existArea = queryAreaUseCase.getById(command.getId());

        Area area = objectMapper.convertValue(command, Area.class);
        area.setId(existArea.getId());
        area.setCode(existArea.getCode());

        areaPort.save(area);
        log.debug("Updated area, id: {}", command.getId());
        return area;
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Deleting area, id: {}", id);
        areaPort.deleteById(id);
        log.debug("Deleted area, id: {}", id);
    }
}
