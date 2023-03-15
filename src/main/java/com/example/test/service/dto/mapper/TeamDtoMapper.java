package com.example.test.service.dto.mapper;

import com.example.test.model.Sports;
import com.example.test.model.Team;
import com.example.test.service.dto.SportsDto;
import com.example.test.service.dto.TeamDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TeamDtoMapper extends BaseMapper<TeamDto, Team> {
    TeamDtoMapper instance = Mappers.getMapper(TeamDtoMapper.class);

    @Override
    @Mapping(source = "sports", target = "sportsDto", qualifiedByName = "entityToDto")
    TeamDto toDto(Team entity);

    @Named("entityToDto")
    public static SportsDto entityToDto(Sports sports) {
        return SportsDtoMapper.instance.toDto(sports);
    }
}
