package com.example.test.user.repository;

import com.example.test.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    public Users findByUsername(String name);

    @Query(value = "select u from Users u join fetch u.team where u.userId = :id")
    public Optional<Users> findByIdFetch(@Param("id") Long id);
}
