package com.example.test.repository;

import com.example.test.model.Sports;
import com.example.test.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SportsRepository extends JpaRepository<Sports, Long> {
}
