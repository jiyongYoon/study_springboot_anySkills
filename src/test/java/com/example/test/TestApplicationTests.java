package com.example.test;

import com.example.test.component.json.JsonConverter;
import com.example.test.jsonmodel.Contract;
import com.example.test.jsonmodel.TestClass;
import com.example.test.model.Sports;
import com.example.test.notification.repository.MemberRepository;
import com.example.test.repository.SportsRepository;
import com.example.test.service.dto.SportsDto;
import com.example.test.service.dto.mapper.SportsDtoMapper;
import com.example.test.user.entity.Member;
import com.example.test.user.entity.Users;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flipkart.zjsonpatch.JsonDiff;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.slf4j.MDC.clear;

@SpringBootTest
class TestApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SportsRepository sportsRepository;

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

        Users newUser = mapper.readValue(userToJson, Users.class);
    }

    @Test
    @DisplayName("JsonDiff")
    void jsonDiff() throws JsonProcessingException, JSONException {
        Users user1 = Users.builder()
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
                .amount(null)
                .manager(Arrays.asList(user2, user1))
//                .createDate(LocalDate.of(2023, 1, 1))
                .build();

        Contract newContract = Contract.builder()
                .id(1)
                .amount(4321L)
                .manager(Arrays.asList(user1, user2))
//                .createDate(LocalDate.of(2023, 2, 5))
                .build();

        TestClass t1 = new TestClass(originalContract);
        TestClass t2 = new TestClass(newContract);

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
//        String s = list.toString();
//        System.out.println("===================v3=======================");
//        System.out.println(s);
//        System.out.println("===================v4======================");
//        System.out.println(differenceMap.values());
//        System.out.println("===================v5======================");
//        System.out.println(differenceMap.keySet());
//        System.out.println("==========================================");

        System.out.println("==========================================");

        ArrayList<String> keys = new ArrayList<>();
        ArrayList<Object> values = new ArrayList<>();
        difference.entriesDiffering().forEach(
            (key, value) -> keys.add(key)
        );
        difference.entriesDiffering().forEach(
            (key, value) -> values.add(value.leftValue())
        );

        System.out.println(keys);
        System.out.println(values);

        JsonConverter jsonConverter = new JsonConverter();
        JSONObject jsonObject = jsonConverter.toJson(keys, values);
        System.out.println(jsonObject.toString());

        System.out.println("==========================================");
        System.out.println();

        Map ToriginalMap = mapper.convertValue(t1, Map.class);
        Map TnewMap = mapper.convertValue(t2, Map.class);

        MapDifference<String, Object> Tdifference = Maps.difference(ToriginalMap, TnewMap);

        ArrayList<String> tKeys = new ArrayList<>();
        ArrayList<Object> tValues = new ArrayList<>();
        Tdifference.entriesDiffering().forEach(
            (key, value) -> tKeys.add(key)
        );
        Tdifference.entriesDiffering().forEach(
            (key, value) -> tValues.add(value.leftValue())
        );

        JSONObject tJsonObject = jsonConverter.toJson(tKeys, tValues);
        System.out.println(tJsonObject.toString());

        Contract jsonContract = mapper.readValue(jsonObject.toString(), Contract.class);
        System.out.println(jsonContract.getAmount());
        System.out.println(jsonContract.getManager().get(0).getUuid());
        System.out.println(jsonContract.getManager().get(0).getUsername());

    }

    @Test
    @DisplayName("jsonObject TEST")
    void jsonObjectTest() throws JsonProcessingException {

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

    @Test
    void ArrayTest() {
        Member member = Member.builder()
                .userId(1L)
                .email("test@email.com")
                .password("123")
                .build();

        List<Long> testList = Arrays.asList(member.getUserId());
        System.out.println(testList);
        System.out.println(testList.size());
    }

    @Test
    void mapTest() {
        Map<Long, Member> longMemberMap = new ConcurrentHashMap<>();
        Member member1 = Member.builder()
                .userId(1L)
                .email("111@email.com")
                .password("123")
                .build();
        Member member2 = Member.builder()
                .userId(2L)
                .email("222@email.com")
                .password("123")
                .build();
        longMemberMap.put(1L, member1);
        longMemberMap.put(2L, member2);

        Member findMember = new Member();
        if(longMemberMap.containsKey(1L)) {
            findMember = longMemberMap.get(1L);
            longMemberMap.remove(1L);
        }
        System.out.println("--------------------------");
//        System.out.println(findMember != null ? findMember.getUserId() : "빈 맴버");
        for (Map.Entry<Long, Member> longMemberEntry : longMemberMap.entrySet()) {
            System.out.println(longMemberEntry.getValue().getUserId());
        }
        System.out.println(longMemberMap.isEmpty());
        System.out.println("--------------------------");

        if(longMemberMap.containsKey(2L)) {
            longMemberMap.remove(2L);
        }

        for (Map.Entry<Long, Member> longMemberEntry : longMemberMap.entrySet()) {
            System.out.println(longMemberEntry.getValue().getUserId());
        }
        System.out.println(longMemberMap.isEmpty());
        System.out.println("--------------------------");

        for (Map.Entry<Long, Member> longMemberEntry : longMemberMap.entrySet()) {
            System.out.println(longMemberEntry.getValue().getUserId());
        }
        System.out.println("--------------------------");

    }

    @Test
    @DisplayName("MapStruct 사용한 SportsDto 매핑")
    public void mapStructTest() {
        // given
        Sports sports = Sports.builder()
                .sportsName("스포츠명")
                .build();
        Sports savedSports = sportsRepository.save(sports);

        // when
        Sports findSports = sportsRepository.findById(savedSports.getSportsId())
                .orElseThrow(RuntimeException::new);

        SportsDto findSportsDto = SportsDtoMapper.instance.toDto(findSports);

        // then
        System.out.println("엔티티명: " + sports.getSportsName());
        System.out.println("저장한 엔티티명: " + savedSports.getSportsName());
        System.out.println("찾은 엔티티명: " + findSports.getSportsName());
        System.out.println("찾은 Dto명: " + findSportsDto.getSportsName());
        assertEquals(findSportsDto.getSportsName(), sports.getSportsName());
    }
}
