package com.example.test.model;

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
    Integer id;
    String name;
    String uuid;

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
