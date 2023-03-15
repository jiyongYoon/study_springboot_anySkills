package com.example.test.service.dto;

import com.example.test.model.Sports;
import com.example.test.model.Team;
import com.example.test.service.dto.mapper.SportsDtoMapper;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamDto {
    private Long teamId;
    private String teamName;
    private SportsDto sportsDto;

    public static TeamDto toDto(Team team) {
        return TeamDto.builder()
                .teamId(team.getTeamId())
                .teamName(team.getTeamName())
                .sportsDto(SportsDtoMapper.instance.toDto(team.getSports()))
                .build();
    }
}
