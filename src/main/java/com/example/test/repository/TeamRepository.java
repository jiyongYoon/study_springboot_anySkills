package com.example.test.repository;

import com.example.test.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query(value = "select t from Team t join fetch t.sports where t.teamId = :teamId")
    Optional<Team> findByIdFetch(@Param("teamId") Long teamId);

    @Query(value = "select t from Team t join fetch t.sports")
    List<Team> findAllFetch();
}
