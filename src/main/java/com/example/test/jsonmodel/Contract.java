package com.example.test.jsonmodel;

import com.example.test.user.entity.Users;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@JsonIgnoreProperties(ignoreUnknown = true)
public class Contract {
    Integer id;
    String status;
    LocalDate createDate;
    List<Users> manager;
    Long amount;

}
