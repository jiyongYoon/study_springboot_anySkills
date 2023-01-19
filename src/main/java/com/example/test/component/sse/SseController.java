package com.example.test.component.sse;

import com.example.test.notification.service.NotificationService;
import com.example.test.user.entity.Users;
import com.example.test.user.model.CustomUserDetails;
import com.example.test.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequiredArgsConstructor
public class SseController {

    private final SseEmitters sseEmitters;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    /*
    직접 message를 작성하여 보내는 메서드
     */
    @GetMapping(value = "/sse-test", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public void publish(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");

        PrintWriter writer = response.getWriter();
        for (int i = 1; i <= 100; i++) {
            writer.write("data: { \"message\" : \"number : " + i
                + "\" }\n\n"); // 데이터 형식은 "data : 실제 응답할 데이터 \n\n"
            // 그러면 보통 모든 작업이 끝난 후 response를 한번에 보내는것과 달리
            // 데이터 형식에 맞는 데이터가 write될 때 마다 해당 데이터가 클라이언트로 전송됨.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        writer.close();
    }

    /*
    java의 SseEmitter를 사용하여 메시지를 보내는 메서드
     */
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect() {
        SseEmitter emitter = new SseEmitter(); // 기본 30초
        sseEmitters.add(emitter); // SseEmitter 객체를 서버에 저장 -> 추후 이벤트 발생 시 클라이언트에게 전송을 위해
        try {
            emitter.send(SseEmitter.event() // SseEmitter 클래스의 빌더메서드
                    .name("connect")
                    .data("connected!"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(emitter);
    }

    @PostMapping(value = "/dispatchEvent", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public void dispatchEventToClients (@RequestParam String contents) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("new_contents")
                        .data(contents));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Void> count() {
        sseEmitters.count();
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public SseEmitter subscribe(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notificationService.subscribe(customUserDetails.getUser().getUserId(), lastEventId);
    }

    @GetMapping(value = "/send", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String send(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Users users = userRepository.findByUsername(customUserDetails.getUsername());
        String contents = "안녕하세용";
        notificationService.send(users, contents);
        return users.getUsername() + "에게 보냄. contents=" + contents;
    }
}
