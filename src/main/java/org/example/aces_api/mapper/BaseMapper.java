package org.example.aces_api.mapper;

import org.mapstruct.factory.Mappers;

import java.util.List;

public interface BaseMapper<E, D> {

//    BaseMapper INSTANCE = Mappers.getMapper(BaseMapper.class);

    D toDto(E entity);
    E toEntity(D dto);
    List<E> toEntity(List<D> dtos);
    List<D> toDto(List<E> dtos);
}
