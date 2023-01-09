package com.example.test.service;

import com.example.test.model.Users;
import com.example.test.repository.UserQuerydslRepository;
import com.example.test.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserQuerydslRepository userQuerydslRepository;

    public Users getUser(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public Users addUser(Integer id, String name) {
        Users addUser = Users.builder()
                .id(id)
                .name(name)
                .build();
        userRepository.save(addUser);
        return addUser;
    }

    public Users updateUserName(Users user, String name) {
        Users updateUser = Users.builder()
                                .id(user.getId())
                                .name(name)
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
                .build();
        userQuerydslRepository.updateUser(updateUser);
    }

}
