package com.example.test.service.dto.mapper;

import com.example.test.model.Sports;
import com.example.test.service.dto.SportsDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SportsDtoMapper extends BaseMapper<SportsDto, Sports> {
    SportsDtoMapper instance = Mappers.getMapper(SportsDtoMapper.class);

}
