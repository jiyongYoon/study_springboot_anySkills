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

import static com.example.test.user.jwt.JwtProperties.*;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    @Value("${jwt.secret.key}")
    private String secretKey;

    private Algorithm getAlgorithm(String secretKey) {
        return Algorithm.HMAC256(secretKey);
    }

    private final UserService userService;

    /*
    JWT 토큰 생성
     */
    public String generateJwtToken(CustomUserDetails userDetails) {
        Date now = new Date();

        return JWT.create()
                .withIssuer(ISSUER)
                .withClaim(ENUM_USER_NAME, userDetails.getUser().getUsername())
                .withClaim(ENUM_EXPIRED_DATE, new Date(now.getTime() + TOKEN_VALID_TIME))
                .sign(getAlgorithm(secretKey));
    }

    /*
    JWT 토큰의 유저명 가져오기
     */
    public String getUsernameByJwtToken(String token) throws Exception {
        DecodedJWT decodedToken = validateToken(token);
        return decodedToken.getClaim(ENUM_USER_NAME).asString();
    }

    /*
    인증 성공 시 권한처리를 위해 세션에 Authentication 객체 생성
     */
    public Authentication getAuthenticationTokenByUsername(String username) throws Exception {
        // 유저정보 가져와서
        CustomUserDetails customUserDetails
                = (CustomUserDetails) userService.loadUserByUsername(username);

        // AuthenticationManager에 넘겨줄 토큰 리턴
        return new UsernamePasswordAuthenticationToken(
                customUserDetails, // 나중에 컨트롤러에서 DI해서 쓸 때 사용하기 편해짐
                null, // 패스워드는 모르니까 null 처리, 어차피 지금 인증하는게 아니니까!!
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
        Date tokenExpiredDate = JWT.decode(token).getClaim(ENUM_EXPIRED_DATE).asDate();
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
