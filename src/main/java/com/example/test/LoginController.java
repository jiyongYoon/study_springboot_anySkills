package com.example.test;

import com.example.test.component.sse.SseEmitters;
import com.example.test.user.entity.Users;
import com.example.test.user.model.CustomUserDetails;
import com.example.test.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @GetMapping("/loginForm")
    public void loginForm() {
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "/joinForm";
    }

    @PostMapping("/join")
    public String join(Users user) {
        System.out.println("회원가입: " + user.getUsername() + " , " + user.getPassword());
        userService.addUser(user.getUsername(), user.getPassword());
        return "redirect:/loginForm";
    }
}
