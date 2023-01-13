package com.example.test.user.service;

import com.example.test.user.entity.Users;
import com.example.test.user.exception.UserNotFoundException;
import com.example.test.user.model.CustomUserDetails;
import com.example.test.user.repository.UserQuerydslRepository;
import com.example.test.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserQuerydslRepository userQuerydslRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Users getUser(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public Users addUser(String name, String password) {
        Users addUser = Users.builder()
                .name(name)
                .password(bCryptPasswordEncoder.encode(password))
                .role("ROLE_USER")
                .build();
        userRepository.save(addUser);
        return addUser;
    }

    public Users updateUserName(Users user, String name) {
        Users updateUser = Users.builder()
                .id(user.getId())
                .name(name)
                .password(user.getPassword())
                .build();
        userRepository.save(updateUser);
        return updateUser;
    }

    public Users getUserByDsl(Integer id) {
        return userQuerydslRepository.getUser(id);
    }

    public void updateUserNameByDsl(Users user, String name) {
        Users updateUser = Users.builder()
                .id(user.getId())
                .name(name)
                .password(user.getPassword())
                .build();
        userQuerydslRepository.updateUser(updateUser);
    }

    @PostConstruct //빈 초기화 콜백 메서드
    public void init() {
        System.out.println("init start.");

    }

    @PreDestroy //빈 소멸전 콜백 메서드
    public void close() {
        System.out.println("Bean close.");

    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Users findUser = userRepository.findByName(username);

        if(findUser != null) {
            System.out.println(findUser.getName() + " 유저정보 존재함");
            return new CustomUserDetails(findUser);
        } else {
            throw new UserNotFoundException(findUser.getName());
        }
    }
}