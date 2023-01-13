package com.example.test.filter;

import javax.servlet.*;
import java.io.IOException;

// 해당 필터는 시큐리티 필터와 별개의 필터패키지임
public class CustomFilter2 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("2번 필터");
        chain.doFilter(request, response); //필터체인에 추가해줘야 다른 작업이 계속 진행됨
    }
}
