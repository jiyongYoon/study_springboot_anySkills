package com.example.test.jsonmodel;

import com.example.test.user.entity.Users;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contract {
    Integer id;
    String status;
    LocalDate createDate;
    Users manager;
    Long amount;

}
