package com.example.test.notification.repository;

import com.example.test.notification.model.Notification;
import com.example.test.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
