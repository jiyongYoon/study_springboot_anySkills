package com.example.test;

import com.example.test.user.entity.Users;
import com.example.test.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
        System.out.println("회원가입: " + user.getName() + " , " + user.getPassword());
        userService.addUser(user.getName(), user.getPassword());
        return "redirect:/loginForm";
    }
}
