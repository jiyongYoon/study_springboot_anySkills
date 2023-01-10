package com.example.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class TestApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @DisplayName("빈 이름 확인")
    void contextLoads() throws Exception {
        if(applicationContext != null) {
            String[] beans = applicationContext.getBeanDefinitionNames();

            for (String bean : beans) {
                System.out.println("bean = " + bean);
            }
        }
    }

}
