package com.example.test.notification.model;

import com.example.test.model.UserStamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Notification extends UserStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long notificationId;

    Long emitterId;
    Long senderId;

    String contents;

    Boolean isRead;

}
