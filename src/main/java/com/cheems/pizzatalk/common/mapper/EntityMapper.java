package com.cheems.pizzatalk.common.mapper;

import java.util.List;
import java.util.Set;

public interface EntityMapper<D, E> {
    E toEntity(D domain);

    D toDomain(E entity);

    List<E> toEntity(List<D> domainList);

    List<D> toDomain(List<E> entityList);

    default Set<String> toEntityAttributes(Set<String> domainAttributes) {
        return domainAttributes;
    }
}
