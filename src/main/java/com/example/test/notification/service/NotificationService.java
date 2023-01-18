package com.example.test.notification.service;

import com.example.test.notification.model.Notification;
import com.example.test.notification.repository.EmitterRepository;
import com.example.test.notification.repository.NotificationRepository;
import com.example.test.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;
    private final long TIME_OUT = 600 * 1000L;

    public SseEmitter subscribe(Integer userId, String lastEventId) {
        String emitterId = makeTimeIncludeId(userId);
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(TIME_OUT));
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        //503 에러 방지를 위해 더미 이벤트 발송 (초기 요청에 내용이 없으면 얼마 안지나서 연결이 끊어짐)
        String eventId = makeTimeIncludeId(userId);
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [UserId=" + userId + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, userId, emitterId, emitter);
        }

        return null;
    }

    private void sendLostData(String lastEventId, Integer userId, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByUserId(String.valueOf(userId));

        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    public void send(Users receiver, String content) {
        Notification notification = notificationRepository.save(createNotification(receiver, content));
        String receiverId = String.valueOf(receiver.getUserId());
        String eventId = receiverId + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByUserId(receiverId);
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendNotification(emitter, eventId, key, notification);
                }
        );
    }

    private Notification createNotification(Users receiver, String content) {
        return Notification.builder()
                .reciever(receiver)
                .contents(content)
                .isRead(false)
                .build();
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .data(data));
        } catch (Exception e) {
            emitterRepository.deleteById(emitterId);
            e.printStackTrace();
        }
    }

    private String makeTimeIncludeId(Integer userId) {
        return userId + "_" + System.currentTimeMillis();
    }
}
