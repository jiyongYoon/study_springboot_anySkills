package com.example.test.user.jwt;

import com.auth0.jwt.JWT;
import com.example.test.user.entity.Users;
import com.example.test.user.model.CustomUserDetails;
import com.example.test.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static com.example.test.user.jwt.JwtProperties.HEADER_STRING;
import static com.example.test.user.jwt.JwtProperties.TOKEN_PREFIX;

@RestController
@RequiredArgsConstructor
public class JwtTestController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @GetMapping("/jwt")
    public String generateJwtToken(@RequestParam String name) throws Exception {
        CustomUserDetails findUserDetails = (CustomUserDetails) userService.loadUserByUsername(name);

        String jwtToken = jwtTokenProvider.generateJwtToken(findUserDetails);

        return jwtToken;
    }

    @GetMapping("/jwt/verify")
    public String validateJwtToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String token = request.getHeader(HEADER_STRING).replace(TOKEN_PREFIX, "");
        String usernameByJwtToken = jwtTokenProvider.getUsernameByJwtToken(token);
        Date expiredDate = JWT.decode(token).getClaim("EXPIRED_DATE").asDate();
        return usernameByJwtToken + "의 토큰은 유효합니다. \n"
                + "유효기간: " + expiredDate.toString();
    }
}
