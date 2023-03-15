package com.example.test.model;

import com.example.test.user.entity.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.AuditOverride;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@DynamicInsert
//@DynamicUpdate
//@AuditOverride(forClass = UserStamp.class)
public class Team /*extends UserStamp*/ implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @NotBlank
    private String teamName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sports_id")
    private Sports sports;

    @ColumnDefault("'팀 설명 없음'")
    private String teamDescription;

    @PrePersist
    private void prePersist() {
        this.teamName = this.teamName == null ? "팀 이름 없이 등록됨" : this.teamName;
    }

    public void setSports(Sports sports) {
        this.sports = sports;
        sports.getTeamList().add(this);
        System.out.println("setSports 메서드 동작");
    }
//    @PreUpdate
//    private void preUpdate() {
//        this.teamName = this.teamName == null ? "팀 이름 없이 수정됨" : this.teamName;
//    }


}
