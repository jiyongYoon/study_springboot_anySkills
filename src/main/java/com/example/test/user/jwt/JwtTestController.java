package com.example.test.user.jwt;

import com.auth0.jwt.JWT;
import com.example.test.user.model.CustomUserDetails;
import com.example.test.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequiredArgsConstructor
public class JwtTestController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @GetMapping("/jwt")
    public String generateJwtToken(@RequestParam String name) {
        CustomUserDetails findUserDetails = (CustomUserDetails) userService.loadUserByUsername(name);

        String jwtToken = jwtTokenProvider.generateJwtToken(findUserDetails);

        return jwtToken;
    }

    @GetMapping("/jwt/verify")
    public String validateJwtToken(@RequestParam String token) throws Exception {
        String usernameByJwtToken = jwtTokenProvider.getUsernameByJwtToken(token);
        Date expiredDate = JWT.decode(token).getClaim("EXPIRED_DATE").asDate();
        return usernameByJwtToken + "의 토큰은 유효합니다. \n"
                + "유효기간: " + expiredDate.toString();
    }

}
