package com.example.test.user.jwt;

import com.example.test.user.entity.Users;
import com.example.test.user.model.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.test.user.jwt.JwtProperties.*;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    /*
    SpringSecurity에 있는 UsernamePasswordAuthenticationFilter는
    /login으로 post요청 시 username, password를 전송하면 해당 필터가 낚아채고
    AuthenticationManager를 사용해서 UserDetailsService로 넘김
        - /login 요청이 되면 attemptAuthentication()로 로그인 시도
        - 로그인이 완료되면 successfulAuthentication()으로 JWT 토큰을 만들어서 클라이언트에게 response 해주면 됨
     */
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    /*
    /login 요청을 하면 로그인 시도를 위해 실행되는 메서드
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter 동작중");

        ObjectMapper objectMapper = new ObjectMapper();
        Users loginUser = new Users();

        /*
        어떻게 로그인 요청을 하느냐에 따라 받는 방식이 달라질 것
        BufferedReader br = request.getReader();

        String input = null;
        while((input = br.readLine()) != null) {
            System.out.println(input);
        }
         */

        /*
        여기서는 json으로 username, password 요청을 받는다고 가정하고 진행
         */
        // 1. username, password를 받아서
        // 1-1. json 데이터를 Users 객체로 파싱
        try {
            loginUser = objectMapper.readValue(request.getInputStream(), Users.class);
            System.out.println(loginUser.getUsername() + ", " + loginUser.getPassword());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 1-2. Users 데이터로 인증객체(Token) 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword());

        // 1-3. AuthenticationManager에게 토큰을 넘겨서 인증 진행(UserDetailsService의 loadUserByUsername() 실행됨)
        Authentication authentication =authenticationManager.authenticate(authenticationToken);
        // 값이 잘 들어왔다는 것은 DB에 있는 username과 password가 일치했다는 뜻
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        System.out.println("로그인 완료됨: " + userDetails.getUsername());
        // -> 인증이 진행되면 해당 객체가 session 영역에 저장됨
        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {
        System.out.println("successfulAuthentication() 실행됨. 즉, 인증이 완료되었다는 뜻");

        CustomUserDetails authUser = (CustomUserDetails) authResult.getPrincipal();

        String jwtToken = jwtTokenProvider.generateJwtToken(authUser);
        System.out.println(TOKEN_PREFIX + jwtToken);
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + jwtToken); // 응답 헤더에 토큰 넣기
    }

    //    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        String jwtToken = jwtTokenProvider.resolveToken((HttpServletRequest) request); // 해더에서 JWT 가져옴
//
//        if (jwtToken != null) {
//            try {
//                jwtTokenProvider.getUsernameByJwtToken(jwtToken); // 검증
//                Authentication authentication = jwtTokenProvider.getAuthentication(jwtToken); // 인증객체 생성
//                SecurityContextHolder.getContext().setAuthentication(authentication); // 세션에 인증객체 저장
//                /*
//                 -> JWT 인데도 세션에 인증객체를 저장하는 이유는 '권한관리' 때문. 만약 권한관리가 필요없다면 션에 담을 필요가 없음
//                 -> Session이 사용되므로 완벽하게 JWT 특성을 활용하지는 못하는 것이므로, 세션없이 stateless하게 관리하기 위해서는
//                 권한관리 로직을 따로 구현해주어야 함
//                 -> 이렇게 되면 세션을 위해 필요한 객체인 UserDetails, UserDetailsService를 구현하지 않아도 되고,
//                 JWT를 활용한 로그인 로직을 모두 구현해주어야 함
//                 -> Security가 해주는 권한처리 지원을 받을 수 없기 때문에 장단점이 있음. (ex, hasRole 등)
//                 Q1) 그렇다면 세션에 관리하는 권한은 서버가 여러개 있을 때 어떻게 처리되는 것인가?
//                      A) 세션에 권한을 담는 작업은 그때그때 하는거라서 괜찮다고 함. -> 실제 구현해봐야 알 것 같음
//                 */
//
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//        chain.doFilter(request, response);
//    }
}
