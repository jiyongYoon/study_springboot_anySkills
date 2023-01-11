package com.example.test.user.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String username) {
        super(username + " NotFoundException");
    }
}
