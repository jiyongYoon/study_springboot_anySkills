package com.example.test.service.dto;

import com.example.test.model.Sports;
import com.example.test.model.Team;
import com.example.test.user.entity.Users;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamDto {
    private Long teamId;
    private String teamName;
    private Sports sports;

    public static TeamDto toDto(Team team) {
        return TeamDto.builder()
                .teamId(team.getTeamId())
                .teamName(team.getTeamName())
                .sports(team.getSports())
                .build();
    }
}
