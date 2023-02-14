package com.example.test.user.service;

import com.example.test.service.dto.UserDto;
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
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserQuerydslRepository userQuerydslRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Users getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public Users addUser(String name, String password) {
        Users addUser = Users.builder()
                .username(name)
                .password(bCryptPasswordEncoder.encode(password))
                .role("ROLE_USER")
                .build();
        userRepository.save(addUser);
        return addUser;
    }

    public Users updateUserName(Users user, String name) {
        Users updateUser = Users.builder()
                .userId(user.getUserId())
                .username(name)
                .password(user.getPassword())
                .build();
        userRepository.save(updateUser);
        return updateUser;
    }

    public Users getUserByDsl(Long id) {
        return userQuerydslRepository.getUser(id);
    }

    public void updateUserNameByDsl(Users user, String name) {
        Users updateUser = Users.builder()
                .userId(user.getUserId())
                .username(name)
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
        System.out.println("========== loadUserByUsername 실행됨 ==========");
        Users findUser = userRepository.findByUsername(username);

        if(findUser != null) {
            return new CustomUserDetails(findUser);
        } else {
            throw new UserNotFoundException(findUser.getUsername());
        }
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserDto::toDto)
                .collect(Collectors.toList());
    }

    public UserDto getUsers(Long id) {
        return UserDto.toDto(userRepository.findById(id).orElseThrow(RuntimeException::new));
    }


    public UserDto createUsers(Users users) {
        return UserDto.toDto(userRepository.save(users));
    }

    @Transactional
    public UserDto updateUsers(Long id, Users users) {
        Users findUsers = userRepository.findById(id).orElseThrow(RuntimeException::new);
        findUsers.setTeam(users.getTeam());
        findUsers.setUsername(users.getUsername());
        return UserDto.toDto(userRepository.save(findUsers));
    }

    public UserDto deleteUsers(Long id) {
        Users findUsers = userRepository.findById(id).orElseThrow(RuntimeException::new);
        userRepository.delete(findUsers);
        return UserDto.toDto(findUsers);
    }
}
