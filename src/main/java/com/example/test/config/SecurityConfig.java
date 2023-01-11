package com.example.test.config;

import com.example.test.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();

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

        // h2-console 엑박 없애줌
        // 시큐리티가 기본적으로 X-Frame-Options Click jacking 공격 막기 설정이 되어 있음
        // 이를 동일 도메인에서는 접근 가능하게 해주면 됨
        // properties로도 가능 (security.headers.frame=false)
        http.headers().frameOptions().sameOrigin();

        return http.build();
    }
}
