package com.example.test.controller;

import com.example.test.model.Users;
import com.example.test.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/id")
    public String printInfo(@RequestParam Integer id) {
        Users user = userService.getUser(id);

        String res = "id = " + id + ", name = " + user.getName();
        System.out.println(res);

        return res;
    }

    @PostMapping("/add/{id}/{name}")
    public String addUser(@PathVariable Integer id,
                          @PathVariable String name) {
        String res = "add id = " + id + ", name: " + name;
        userService.addUser(id, name);
        System.out.println(res);
        return res;
    }

    @PostMapping("/id/{id}")
    public String changeName(@PathVariable Integer id,
                             @RequestBody String name) {
        Users user = userService.getUser(id);
        String beforeName = user.getName();
        String res = "target id = " + id + ", name: " + beforeName + " -> " + name;
        userService.updateUserName(user, name);
        System.out.println(res);
        return res;
    }

    @GetMapping("/dsl/{id}")
    public String printInfoByDsl(@PathVariable Integer id) {
        Users user = userService.getUserByDsl(id);

        String res = "id = " + id + ", name = " + user.getName();
        System.out.println(res);

        return res;
    }

    @Transactional
    @PostMapping("/dsl")
    public String changeNameByDsl(@RequestParam Integer id,
                                  @RequestBody String name) {
        Users user = userService.getUserByDsl(id);
        String beforeName = user.getName();
        String res = "target id = " + id + ", name: " + beforeName + " -> " + name;
        userService.updateUserNameByDsl(user, name);
        System.out.println(res);
        return res;
    }

}
