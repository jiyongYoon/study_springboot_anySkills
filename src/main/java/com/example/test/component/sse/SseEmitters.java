package com.example.test.component.sse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class SseEmitters {

    private static final AtomicLong counter = new AtomicLong();
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter add(SseEmitter emitter) {
        this.emitters.add(emitter);
        System.out.println("new emitter added: " + emitter);
        System.out.println("emitter list size: " + emitters.size());
        emitter.onCompletion(() -> {
            System.out.println("onCompletion callback");
            this.emitters.remove(emitter);
        });
        emitter.onTimeout(() -> {
            System.out.println("onTimeout callback");
            emitter.complete();
        });

        return emitter;
    }

    public void count() {
        long count = counter.incrementAndGet();
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("count")
                        .data(count));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public long getCount() {
        return counter.get();
    }
}
