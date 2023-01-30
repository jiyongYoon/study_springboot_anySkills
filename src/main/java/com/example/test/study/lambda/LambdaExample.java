package com.example.test.study.lambda;

/*
형태는 매개변수를 가진 코드 블럭이지만,
런타임 시에는 익명 클래스를 생성하여 동작.
함수 지향 언어에 가까움.
(타입 매개변수) -> {실행문;} 형태로 작성

- 장점
지연연산 수행가능하며, 병렬처리 가능
- 단점
람다 stream으로 단순 반복문 사용시 성능 저하
 */

interface LambdaTest1 {
    int addOne(int i);
}

interface LambdaTest2 {
    int sum(int a, int b);
}

public class LambdaExample {

    public static void main(String[] args) {
        LambdaTest1 test1 = i -> ++i;
        System.out.println(test1.addOne(1)); // 2

        test1 = i -> i + 2;
        System.out.println(test1.addOne(1)); // 3

        LambdaTest2 test2 = (a, b) -> a + b;
        System.out.println(test2.sum(3, 4)); // 7

        LambdaTest2 test3 = Integer::sum;
        System.out.println(test3.sum(3, 4)); // 7
    }

}
