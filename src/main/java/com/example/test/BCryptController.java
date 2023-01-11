package com.example.test;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BCryptController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/bcrypt/{row}")
    public String encode(@PathVariable String row) {
        return bCryptPasswordEncoder.encode(row);
    }

}
