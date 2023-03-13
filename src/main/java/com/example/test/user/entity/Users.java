package com.example.test.user.entity;

import com.example.test.model.Team;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@AuditOverride(forClass = TimeStamp.class)
public class Users /*extends TimeStamp*/ implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String username;
    private String password;
    private String role;
    private String uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    @JsonIgnore
    private Team team;

    public Users(Long id, String name) {
        this.userId = id;
        this.username = name;
    }

    @PrePersist
    public void printPreTime() {
        UUID uuid1 = UUID.randomUUID();
        this.uuid = String.valueOf(uuid1);
        System.out.println(uuid1);
    }

    @PostPersist
    public void printPostTime() {
        System.out.println(this.uuid);
    }

    @PreUpdate
    public void preUpdate() {
        System.out.println("업데이트 시작");
    }

    @PostUpdate
    public void postUpdate() {
        System.out.println("업데이트 완료");
    }

}
