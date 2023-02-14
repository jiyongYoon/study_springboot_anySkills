package com.example.test.service.dto;

import com.example.test.model.Team;
import com.example.test.user.entity.Users;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long userId;
    private String username;
    private String password;
    private Team team;

    public static UserDto toDto(Users users) {
        return UserDto.builder()
                .userId(users.getUserId())
                .username(users.getUsername())
                .password(users.getPassword())
                .team(users.getTeam())
                .build();
    }
}
