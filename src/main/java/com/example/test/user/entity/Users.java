package com.example.test.user.entity;

import com.example.test.model.BaseEntity;
import lombok.*;
import org.hibernate.envers.AuditOverride;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Users extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer userId;
    String username;
    String password;
    String role;
    String uuid;

    public Users(Integer id, String name) {
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
