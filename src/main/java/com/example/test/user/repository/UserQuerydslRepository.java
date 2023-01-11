package com.example.test.user.repository;

import com.example.test.user.entity.QUsers;
import com.example.test.user.entity.Users;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class UserQuerydslRepository {

    private final JPAQueryFactory queryFactory;
    QUsers user = new QUsers("U");


    public Users getUser(Integer id) {
        return queryFactory
                .select(Projections.constructor(Users.class,
                        user.id,
                        user.name
                        )
                )
                .from(user)
                .where(user.id.eq(id))
                .fetchOne();
    }

    public void updateUser(Users updateUser) {
        queryFactory
                .update(user)
                .where(user.id.eq(updateUser.getId()))
                .set(user.name, updateUser.getName())
                .set(user.updateDt, LocalDateTime.now())
                .execute();
    }
}
