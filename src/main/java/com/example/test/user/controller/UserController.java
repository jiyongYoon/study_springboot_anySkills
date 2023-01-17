package com.example.test.user.controller;

import com.example.test.user.entity.Users;
import com.example.test.user.model.CustomUserDetails;
import com.example.test.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    public String printInfo(@RequestParam Integer id) {
        Users user = userService.getUser(id);

        String res = "id = " + id + ", name = " + user.getUsername();
        System.out.println(res);

        return res;
    }

    @GetMapping("/whoami")
    public String printMyInfo(@AuthenticationPrincipal CustomUserDetails user) {
        return "id = " + user.getUser().getUserId() + ", Username = " + user.getUsername();
    }

    @PutMapping("/user")
    public String changeName(@RequestParam Integer id,
                             @RequestBody String name) {
        Users user = userService.getUser(id);
        String beforeName = user.getUsername();
        String res = "target id = " + id + ", name: " + beforeName + " -> " + name;
        userService.updateUserName(user, name);
        System.out.println(res);
        return res;
    }

    @GetMapping("/info/dsl")
    public String printInfoByDsl(@RequestParam Integer id) {
        Users user = userService.getUserByDsl(id);

        String res = "id = " + id + ", name = " + user.getUsername();
        System.out.println(res);

        return res;
    }

    @Transactional
    @PutMapping("/user/dsl")
    public String changeNameByDsl(@RequestParam Integer id,
                                  @RequestBody String name) {
        Users user = userService.getUserByDsl(id);
        String beforeName = user.getUsername();
        String res = "target id = " + id + ", name: " + beforeName + " -> " + name;
        userService.updateUserNameByDsl(user, name);
        System.out.println(res);
        return res;
    }

}
