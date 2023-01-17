package com.example.test.user.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.test.user.jwt.JwtProperties.*;

/*
시큐리티 필터 중에 BasicAuthenticationFilter가 있는데
인증이나 인가가 필요한 요청이 오면 해당 필터를 무조건 타게 됨
인증이나 인가가 필요없는 요청이면 필터를 안탐
 */

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private JwtTokenProvider jwtTokenProvider;

    public JwtAuthorizationFilter(JwtTokenProvider jwtTokenProvider,
                                  AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("========== BasicAuthenticationFilter. 인증이나 인가가 필요한 요청이 옴 ==========");
        // 1. 요청의 헤더를 까봄
        String header = request.getHeader(HEADER_STRING);
        if(header == null || !header.startsWith(TOKEN_PREFIX)) {
            System.out.println("header가 없거나 JWT 토큰이 아님");
            chain.doFilter(request, response);
            return;
        }
        System.out.println("header: " + header);
        // 2. 헤더가 jwt 토큰이 맞으면 해당 토큰으로 유효성 검증을 하고 맞으면 username을 가져옴
        // 여기서 통과하면 jwt 토큰으로 인가는 완료됨
        String requestToken = header.replace(TOKEN_PREFIX, "");
        String username = null;
        try {
            username = jwtTokenProvider.getUsernameByJwtToken(requestToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 3. 권한처리를 위해 Authentication 객체를 받아와서 세션에 저장 // ROLE_USER와 같은 것
        // 매번 저장하기 때문에 서버가 나뉘어있어도 상관이 없음
        try {
            Authentication authenticationToken = jwtTokenProvider.getAuthenticationTokenByUsername(username);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken); // 세션에 저장
        } catch (Exception e) {
            e.printStackTrace();
        }

        chain.doFilter(request, response);
    }
}
