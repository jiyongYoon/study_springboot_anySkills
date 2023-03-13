package com.example.test.service.dto;

import com.example.test.model.Team;
import com.example.test.user.entity.Users;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private Long userId;
    private String username;
    private String password;
    private Team team;

    public static UserResponseDto toDto(UserDto users) {
        return UserResponseDto.builder()
                .userId(users.getUserId())
                .username(users.getUsername())
                .password(users.getPassword())
                .team(users.getTeam())
                .build();
    }
}
