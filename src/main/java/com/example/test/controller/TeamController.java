package com.example.test.controller;

import com.example.test.model.Team;
import com.example.test.service.TeamService;
import com.example.test.service.dto.TeamDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    public List<TeamDto> getAllTeams() {
        return teamService.getAllTeams();
    }

    @GetMapping("/fetch")
    public List<TeamDto> getAllTeamsFetch() {
        return teamService.getAllTeamsFetch();
    }

    @GetMapping("/{id}")
    public TeamDto getTeam(@PathVariable Long id) {
        return teamService.getTeam(id);
    }

    @GetMapping("/fetch/{id}")
    public TeamDto getTeamFetch(@PathVariable Long id) {
        return teamService.getTeamFetch(id);
    }

    @PostMapping
    public TeamDto createTeam(@RequestBody Team team) {
        return teamService.createTeam(team);
    }

    @PutMapping("/{id}")
    public TeamDto updateTeam(@PathVariable Long id, @RequestBody TeamDto team) {
        return teamService.updateTeam(id, team);
    }

    @DeleteMapping("/{id}")
    public TeamDto deleteTeam(@PathVariable Long id) {
        return teamService.deleteTeam(id);
    }

    @GetMapping("/prePersist")
    public void prePersist() {
        teamService.prePersist();
    }
}
