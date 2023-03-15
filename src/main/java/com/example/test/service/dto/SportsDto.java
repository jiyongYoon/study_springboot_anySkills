package com.example.test.service.dto;

import com.example.test.model.Sports;
import com.example.test.model.Team;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SportsDto {
    private Long sportsId;
    private String sportsName;

    public static SportsDto toDto(Sports sports) {
        return SportsDto.builder()
                .sportsId(sports.getSportsId())
                .sportsName(sports.getSportsName())
                .build();
    }
}
