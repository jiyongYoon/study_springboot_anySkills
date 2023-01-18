package com.example.test.config;

import com.example.test.filter.CustomFilter3;
import com.example.test.user.jwt.JwtAuthenticationFilter;
import com.example.test.user.jwt.JwtAuthorizationFilter;
import com.example.test.user.jwt.JwtTokenProvider;
import com.example.test.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();

        // h2-console 엑박 없애줌
        // 시큐리티가 기본적으로 X-Frame-Options Click jacking 공격 막기 설정이 되어 있음
        // 이를 동일 도메인에서는 접근 가능하게 해주면 됨
        // properties로도 가능 (security.headers.frame=false)
        http.headers().frameOptions().sameOrigin();

        /*
        formLogin 사용 시 설정
         */

        http.authorizeRequests()
                .antMatchers("/user/**")
                    .authenticated()
                .anyRequest()
                    .permitAll()
            .and()
                .formLogin()
                    .loginPage("/loginForm")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/");


        /*
        JWT 토큰 사용 시 설정
         */
        /*
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                        .formLogin().disable()
                        .httpBasic().disable()
                        .apply(new AddCustomFilter()) // 커스텀 필터 등록(JWT 관련 필터들)
                .and()
                        .authorizeRequests()
                                .antMatchers("/jwt/**")
                                        .authenticated()
                                                .anyRequest().permitAll();

        // 커스텀 필터를 시큐리티 필터들에 등록 -> 커스텀필터 1, 2보다 먼저 실행됨 -> 시큐리티 필터가 다른 필터들보다 먼저 실행됨
        http.addFilterBefore(
                new CustomFilter3(), UsernamePasswordAuthenticationFilter.class
        );
         */

        return http.build();
    }

    public class AddCustomFilter extends AbstractHttpConfigurer<AddCustomFilter, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

            http.addFilter(new JwtAuthenticationFilter(jwtTokenProvider, authenticationManager))
                .addFilter(new JwtAuthorizationFilter(jwtTokenProvider, authenticationManager));
        }
    }

    /*
    AuthenticationManager Bean 등록 -> 해당 메니저는 UsernamePasswordAuthenticationFilter 가 로그인을 처리할 때 사용하기 위해서
    빈으로 주입하지 않고 Filter 생성자에 직접 주입도 가능 (http.getSharedObject(AuthenticationManager.class)

    @Bean -> Security 버전이 낮아 WebSecurityConfigurerAdapter 를 상속했을때는 이 메서드처럼 빈등록 가능
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
   }
   */

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http)
            throws Exception {
        return http.getSharedObject(AuthenticationManager.class);
    }
}
