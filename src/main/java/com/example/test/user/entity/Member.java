package com.example.test.user.entity;

import com.example.test.model.Team;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(nullable = false, length = 30)
    private String email;

    private String password;

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Member createMember(String email, String password) {
        return new Member(email, password);
    }
}
