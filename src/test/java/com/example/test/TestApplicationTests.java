package com.example.test;

import com.example.test.jsonmodel.Contract;
import com.example.test.jsonmodel.JsonDto;
import com.example.test.notification.repository.MemberRepository;
import com.example.test.user.entity.Member;
import com.example.test.user.entity.Users;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flipkart.zjsonpatch.JsonDiff;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.slf4j.MDC.clear;

@SpringBootTest
class TestApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MemberRepository memberRepository;

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

    @Test
    @DisplayName("json 직렬화 및 역직렬화")
    void fromJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());;

        Users user = Users.builder()
                .userId(1L)
                .username("첫째")
                .role("ROLE_USER")
                .build();

        String userToJson = mapper.writeValueAsString(user);

        System.out.println(userToJson);
    }

    @Test
    @DisplayName("JsonDiff")
    void jsonDiff() {
        Users user1 = Users.builder()
                .userId(1L)
                .username("윤지용")
                .role("사원")
                .build();

        Users user2 = Users.builder()
                .userId(2L)
                .username("홍길동")
                .role("대리")
                .build();

        Contract originalContract = Contract.builder()
                .id(1)
                .status("ING")
                .amount(123456L)
                .manager(user1)
//                .createDate(LocalDate.of(2023, 1, 1))
                .build();

        Contract newContract = Contract.builder()
                .id(1)
                .amount(4321L)
                .manager(user2)
//                .createDate(LocalDate.of(2023, 2, 5))
                .build();

        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());;
        JsonNode diffNode = JsonDiff.asJson(
                mapper.valueToTree(originalContract),
                mapper.valueToTree(newContract)
        );
        System.out.println("====== JsonDiff =====");
        System.out.println(diffNode.toPrettyString());
        System.out.println(diffNode);
        System.out.println("==========================================");

        Map originalMap = mapper.convertValue(originalContract, Map.class);
        Map newMap = mapper.convertValue(newContract, Map.class);

        MapDifference<String, Object> difference = Maps.difference(originalMap, newMap);
        Map<String, MapDifference.ValueDifference<Object>> differenceMap = difference.entriesDiffering();

        System.out.println("====== MapDifference =====");
        System.out.println("===================v1======================");
        difference.entriesDiffering().forEach(
                (key, value) -> System.out.println(key + ": " + value)
        );
        System.out.println("===================v2======================");
        List<String> list = new ArrayList<>();
        difference.entriesDiffering().forEach(
                (key, value) -> {
                    String print = "{\"" + key + "\":\"" + value.rightValue() + "\"}";
                    list.add(print);
                    System.out.println(print);
                }
        );
        String s = list.toString();
        System.out.println("==========================================");
        System.out.println(s);
        System.out.println("==========================================");

    }

    @Test
    void uniqueEmailTest() {
        //given
        Member member = new Member();
        Member member1 = memberRepository.save(member.createMember("email", "password"));
        clear();

        Member member2 = memberRepository.save(member.createMember(member1.getEmail(), "password2"));
        System.out.println(member1.getEmail());
        System.out.println(member2.getEmail());
    }
}
