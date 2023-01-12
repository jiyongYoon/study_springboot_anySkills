package com.example.test.user.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.test.user.model.CustomUserDetails;
import com.example.test.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    private final int JWT_TOKEN_VALID_MILLI_SEC = 1000;
    private final int SEC = JWT_TOKEN_VALID_MILLI_SEC;
    private final int MINUTE = 60 * SEC;
    private final int HOUR = 60 * MINUTE;
    private final int DAY = 24 * HOUR;

    private long tokenValidTime = 30 * MINUTE;
    @Value("${jwt.secret.key}")
    private String secretKey;
    private final String ISSUER = "jyyoon";
    private final String USER_NAME = "USER_NAME";
    private final String EXPIRED_DATE = "EXPIRED_DATE";
    private Algorithm getAlgorithm(String secretKey) {
        return Algorithm.HMAC256(secretKey);
    }

    private final UserService userService;

    // JWT 토큰 생성
    public String generateJwtToken(CustomUserDetails userDetails) {
        Date now = new Date();

        String jwtToken = JWT.create()
                .withIssuer(ISSUER)
                .withClaim(USER_NAME, userDetails.getUser().getName())
                .withClaim(EXPIRED_DATE, new Date(now.getTime() + tokenValidTime))
                .sign(getAlgorithm(secretKey));
        System.out.println(new String(Base64.getDecoder().decode(secretKey)));
        System.out.println(jwtToken);

        return jwtToken;
    }

    // JWT 토큰의 유저명 가져오기
    public String getUsernameByJwtToken(String token) throws Exception {
        DecodedJWT decodedToken = validateToken(token);
        return decodedToken.getClaim(USER_NAME).asString();
    }

    /*
    인증 성공 시 세션에 Authentication 객체 생성
     */
    public Authentication getAuthentication(String token) throws Exception {
        CustomUserDetails customUserDetails
                = (CustomUserDetails) userService.loadUserByUsername(getUsernameByJwtToken(token));

        return new UsernamePasswordAuthenticationToken(
                customUserDetails,
                "",
                customUserDetails.getAuthorities()
        );
    }

    /*
    JWT 토큰 validate
    1. 유효기간 검사
    2. 서명 검사
     */
    private DecodedJWT validateToken(String token) throws Exception {
        Date now = new Date();
        Date tokenExpiredDate = JWT.decode(token).getClaim(EXPIRED_DATE).asDate();
        if (tokenExpiredDate.before(now)) {
            throw new Exception("유효기간이 지난 토큰입니다. 유효기간: " + tokenExpiredDate);
        }

        JWTVerifier verifier = JWT
                .require(getAlgorithm(secretKey))
                .build();

        return verifier.verify(token);
    }

    /*
    초기화 시 시크릿 키 Base64 인코딩
     */
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

}
