package com.example.test.config;

import com.example.test.filter.CustomFilter1;
import com.example.test.filter.CustomFilter2;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<CustomFilter1> filter1() {
        FilterRegistrationBean<CustomFilter1> bean = new FilterRegistrationBean<>(new CustomFilter1());
        bean.addUrlPatterns("/*"); // 필터를 적용시킬 URL
        bean.setOrder(0); // 필터 순서. 낮은 번호가 우선순위
        return bean;
    }

    @Bean
    public FilterRegistrationBean<CustomFilter2> filter2() {
        FilterRegistrationBean<CustomFilter2> bean = new FilterRegistrationBean<>(new CustomFilter2());
        bean.addUrlPatterns("/*"); // 필터를 적용시킬 URL
        bean.setOrder(1); // 필터 순서. 낮은 번호가 우선순위
        return bean;
    }
}
