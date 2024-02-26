package com.cheems.pizzatalk.modules.area.adapter.database;

import com.cheems.pizzatalk.entities.mapper.AreaMapper;
import com.cheems.pizzatalk.modules.area.application.port.out.AreaPort;
import com.cheems.pizzatalk.modules.area.domain.Area;
import com.cheems.pizzatalk.repository.AreaRepository;
import org.springframework.stereotype.Component;

@Component
public class AreaAdapter implements AreaPort {

    private final AreaRepository areaRepository;

    private final AreaMapper areaMapper;

    public AreaAdapter(AreaRepository areaRepository, AreaMapper areaMapper) {
        this.areaRepository = areaRepository;
        this.areaMapper = areaMapper;
    }

    @Override
    public Area save(Area area) {
        return areaMapper.toDomain(areaRepository.save(areaMapper.toEntity(area)));
    }

    @Override
    public void deleteById(Long id) {
        areaRepository.deleteById(id);
    }
}
