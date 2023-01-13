package com.example.test.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 해당 필터는 시큐리티 필터와 별개의 필터패키지임
public class CustomFilter3 implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

//        printHeaderInfo(httpServletRequest);

//        System.out.println("3번 필터 - 시큐리티 필터 UsernamePasswordAuthenticationFilter 앞에 등록");
        chain.doFilter(request, response); //필터체인에 추가해줘야 다른 작업이 계속 진행됨
    }

    private void printHeaderInfo(HttpServletRequest httpServletRequest) {
        System.out.println("http 메서드: " + httpServletRequest.getMethod());
        System.out.println("http Cookie 헤더: " + httpServletRequest.getHeader("Cookie"));
        System.out.println("http User-Agent 헤더: " + httpServletRequest.getHeader("User-Agent"));
        System.out.println("http Authorization 헤더: " + httpServletRequest.getHeader("Authorization"));
    }
}
