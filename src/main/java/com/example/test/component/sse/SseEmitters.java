package com.example.test.component.sse;

import com.example.test.notification.model.Notification;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class SseEmitters {

    private static final AtomicLong counter = new AtomicLong();
    private static final AtomicLong counter1 = new AtomicLong();
    private static final AtomicLong counter2 = new AtomicLong();
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    /*
    아이디 별로 Emitter를 열어서 해당 유저에게만 해당 메시지를 보내기 위한 해시맵
     */
    public Map<Long, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    /*
    해당 Emitter에 접속자 수를 확인해야하며, 여러명이 접속해야 하는 경우
    (즉, 개인 Noti가 아니라 구독형으로 Noti가 가게 된다면)
    Map 뒤에 SseEmitter를 리스트로 넣고, 해당 아이디의 Emitter 리스트를 찾아서
    순환하면서 메시지를 모두 보내면 됨.
     */
    public Map<Long, List<SseEmitter>> emitterListMap = new ConcurrentHashMap<>();



    public SseEmitter add(SseEmitter emitter, Long id) {
        if(emitterListMap.get(id) == null) {
            List<SseEmitter> list = new CopyOnWriteArrayList<>();
            list.add(emitter);
            emitterListMap.put(id, list);
        } else {
            emitterListMap.get(id).add(emitter);
        }
//        this.emitterMap.put(id, emitter);
//        this.emitters.add(emitter);
        System.out.println("new emitter added: " + emitter);
//        System.out.println("emitter list size: " + emitters.size());
        emitter.onCompletion(() -> {
            System.out.println("onCompletion callback");
//            this.emitters.remove(emitter);
//            emitter.complete();
        });
        emitter.onTimeout(() -> {
            System.out.println("onTimeout callback");
//            emitter.complete();
//            this.emitters.remove(emitter);

        });

        return emitter;
    }

    public void count(Long emitterId, Integer senderId) {
        long count = counter.incrementAndGet();
        Notification noti = Notification.builder()
                .emitterId(emitterId)
                .senderId(senderId)
                .isRead(false)
                .contents(String.valueOf(count))
                .build();
        String eventName = "count" + String.valueOf(emitterId);
        emitterListMap.get(emitterId).forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name(eventName)
                        .data(noti));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void count1(Integer senderId) {
        long count = counter1.incrementAndGet();
        Notification noti = Notification.builder()
                .senderId(senderId)
                .isRead(false)
                .contents(String.valueOf(count))
                .build();
        emitterListMap.get(1L).forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("count1")
                        .data(noti));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        /*
        try {
            emitter.send(SseEmitter.event()
                    .name("count1")
                    .data(count));
        } catch (IOException e) {
            e.printStackTrace();
        }
         */
        /*
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("count")
                        .data(count));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
         */
    }

    public void count2(Integer senderId) {
        long count = counter2.incrementAndGet();
        Notification noti = Notification.builder()
                .senderId(senderId)
                .isRead(false)
                .contents(String.valueOf(count))
                .build();
        emitterListMap.get(2L).forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("count2")
                        .data(noti));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        /*
        SseEmitter emitter = emitterMap.get(2L);
        try {
            emitter.send(SseEmitter.event()
                    .name("count2")
                    .data(count));
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        /*
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("count")
                        .data(count));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
         */
    }
}
