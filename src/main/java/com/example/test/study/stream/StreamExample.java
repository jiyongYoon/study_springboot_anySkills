package com.example.test.study.stream;


/*
배열, 컬렉션 등의 데이터를 하나씩 참조하여 처리하는 기능을 가진 클래스
데이터소스객체.stream생성().중개연산().최종연산(); 구조로 이루어짐.

1. 생성
- 컬랙션, 배열, 가변 매개변수, 지정된 범위의 연속된 정수, 난수, 람다 표현식, 파일, 빈 스트림 생성 가능
2. 중개연산
- filter() : 주어진 조건에 참인 요소 필터
- distinct() : 스트림의 중복 요소 제거
- map() : map에 명령문을 인수로 전달하여 반환값들로 구성된 새로운 스트림 반환
- flatMap() : map과 인수전달 동일. 스트림 요소가 배열인 경우, 주이전 명령문을 적용한 새로운 스트림으로 반환
- sort() : Comparator를 구현하여 넘겨주거나, natural order로 정렬
- limit() : 스트림에서 limit에 들어가는 수만큼의 새로운 스트림을 반환
- skip() : 스트림의 첫 요소부터 skip에 들어가는 수를 제외한 새로운 스트림 반환
- peek() : 각 요소를 돌면서 peek에 전달된 명령문 수행
3. 최종연산
- count(), min(), max()
- sum(), average()
- collect() : 매개변수로 Collectors의 구현 메서드를 받아 메서드의 동작대로 요소를 수집하여 반환
- reduce() : 스트림의 첫 두 요소를 가지고 연산 후, 이후 요소들로 연산한 값을 반환
- anyMatch(), allMatch(), noneMatch(), findFirst(), findAny()[이건 parallelStream 경우에 사용]
- forEach() : 각 요소를 돌면서 forEach()에 전달된 명령문을 수행
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

public class StreamExample {

    public static void main(String[] args) {

        String[] arr = {"apple", "banana", "cat", "dog", "dog"};
        // 스트림 생성
        Stream<String> stringStream = Arrays.stream(arr);
        // 스트림 중개 연산
        stringStream.filter(str -> str.contains("c")).forEach(System.out::println);
        // -> 위에서 스트림을 사용했기 때문에 다시 만들어주지 않으면 Exception 터짐.
        stringStream = Arrays.stream(arr);
        stringStream.distinct().forEach(System.out::println);


        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5);
        // 스트림 생성
        Stream<Integer> integerStream = integerList.stream();
        // 스트림 중개 연산
        integerList.stream().map(integer -> integer + 5).forEach(System.out::println);


        int[] data = {5, 6, 4, 2, 3, 1, 1, 2, 2, 4, 8};

        // 짝수만 포함하는 리스트 생성
        ArrayList<Integer> dataList = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            if(data[i] % 2 == 0) {
                dataList.add(data[i]);
            }
        }

        // 중복 제거
        HashSet<Integer> dataSet = new HashSet<>(dataList);

        // List로 다시 변경
        ArrayList<Integer> distinctList = new ArrayList<>(dataSet);

        // 역순 정렬
        distinctList.sort(Comparator.reverseOrder());

        // int[] 배열로 반환
        int[] result = new int[distinctList.size()];
        for (int i = 0; i < distinctList.size(); i++) {
            result[i] = distinctList.get(i);
        }

        int[] result2 = Arrays.stream(data)
            .boxed() // Stream<Integer>로
            .filter(n -> n % 2 == 0)
            .distinct()
            .sorted(Comparator.reverseOrder())
            .mapToInt(Integer::intValue) // IntStream으로
            .toArray(); // int[] 배열로

    }

}
