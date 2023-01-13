package com.example.test.user.jwt;

public interface JwtProperties {
    int JWT_TOKEN_VALID_MILLI_SEC = 1000;
    int SEC = JWT_TOKEN_VALID_MILLI_SEC;
    int MINUTE = 60 * SEC;
    int HOUR = 60 * MINUTE;
    int DAY = 24 * HOUR;


    long TOKEN_VALID_TIME = 30 * MINUTE;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    String ISSUER = "jyyoon";



    String ENUM_USER_NAME = "USER_NAME";
    String ENUM_EXPIRED_DATE = "EXPIRED_DATE";
}
