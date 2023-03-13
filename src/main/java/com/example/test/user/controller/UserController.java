package com.example.test.user.controller;

import com.example.test.model.Team;
import com.example.test.service.dto.UserDto;
import com.example.test.service.dto.UserResponseDto;
import com.example.test.user.entity.Users;
import com.example.test.user.model.CustomUserDetails;
import com.example.test.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUsers(@PathVariable Long id) {
        return userService.getUsers(id);
    }

    @GetMapping("/fetch/{id}")
    public UserDto getUsersFetch(@PathVariable Long id) {
        return userService.getUserFetch(id);
    }

    @GetMapping("/resDto/{id}")
    public UserResponseDto getUsersRes(@PathVariable Long id) {
        return userService.getUserRes(id);
    }

    @PostMapping
    public UserDto createUsers(@RequestBody Users users) {
        return userService.createUsers(users);
    }

    @PutMapping("/{id}")
    public UserDto updateUsers(@PathVariable Long id, @RequestBody Users users) {
        return userService.updateUsers(id, users);
    }

    @DeleteMapping("/{id}")
    public UserDto deleteUsers(@PathVariable Long id) {
        return userService.deleteUsers(id);
    }

    @GetMapping("/info")
    public String printInfo(@RequestParam Long id) {
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
    public String changeName(@RequestParam Long id,
                             @RequestBody String name) {
        Users user = userService.getUser(id);
        String beforeName = user.getUsername();
        String res = "target id = " + id + ", name: " + beforeName + " -> " + name;
        userService.updateUserName(user, name);
        System.out.println(res);
        return res;
    }

    @GetMapping("/info/dsl")
    public String printInfoByDsl(@RequestParam Long id) {
        Users user = userService.getUserByDsl(id);

        String res = "id = " + id + ", name = " + user.getUsername();
        System.out.println(res);

        return res;
    }

    @Transactional
    @PutMapping("/user/dsl")
    public String changeNameByDsl(@RequestParam Long id,
                                  @RequestBody String name) {
        Users user = userService.getUserByDsl(id);
        String beforeName = user.getUsername();
        String res = "target id = " + id + ", name: " + beforeName + " -> " + name;
        userService.updateUserNameByDsl(user, name);
        System.out.println(res);
        return res;
    }

}
