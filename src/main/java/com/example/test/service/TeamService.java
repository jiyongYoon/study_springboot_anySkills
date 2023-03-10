package com.example.test.service;

import com.example.test.model.Sports;
import com.example.test.model.Team;
import com.example.test.repository.SportsRepository;
import com.example.test.repository.TeamRepository;
import com.example.test.service.dto.TeamDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final SportsRepository sportsRepository;

    @Transactional
    public List<TeamDto> getAllTeams() {
//        List<Team> teamList = teamRepository.findAll();
//        List<TeamDto> teamDtoList = new ArrayList<>();
//        for (int i = 0; i < teamList.size(); i++) {
//            Team curTeam = teamList.get(i);
//            if(i == 0) {
//                System.out.println(curTeam.getSports().getSportsName());
//            }
//            teamDtoList.add(TeamDto.toDto(curTeam));
//        }
//        return teamDtoList;
        return teamRepository.findAll().stream()
                .map(TeamDto::toDto)
                .collect(Collectors.toList());
    }
    @Transactional
    public List<TeamDto> getAllTeamsFetch() {
        return teamRepository.findAllFetch().stream()
                .map(TeamDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TeamDto getTeam(Long id) {

        return TeamDto.toDto(teamRepository.findById(id).orElseThrow(RuntimeException::new));
    }

    @Transactional
    public TeamDto getTeamFetch(Long id) {
        return TeamDto.toDto(teamRepository.findByIdFetch(id).orElseThrow(RuntimeException::new));
    }

    public TeamDto createTeam(Team team) {
        return TeamDto.toDto(teamRepository.save(team));
    }

    @Transactional
    public TeamDto updateTeam(Long id, TeamDto team) {
        Sports findSports = sportsRepository.findById(team.getSports().getSportsId())
                .orElseThrow(RuntimeException::new);
        Team findTeam = teamRepository.findById(id)
                .orElseThrow(RuntimeException::new);
        findTeam.setTeamName(team.getTeamName());
        findTeam.setSports(findSports);
        return TeamDto.toDto(teamRepository.save(findTeam));
    }

    public TeamDto deleteTeam(Long id) {
        Team findTeam = teamRepository.findById(id)
                .orElseThrow(RuntimeException::new);
        teamRepository.delete(findTeam);
        return TeamDto.toDto(findTeam);
    }

    public void prePersist() {
        Team teamA = new Team();
        Team teamB = Team.builder()
                .teamName("????????? ???")
                .build();

        teamA = teamRepository.saveAndFlush(teamA);
        teamB = teamRepository.saveAndFlush(teamB);

        System.out.println("------??? ?????? ??? ??? ??????------");
        System.out.println(teamA.getTeamName());
        System.out.println(teamB.getTeamName());

        teamA.setTeamName("???A ?????? ??????");
        teamB.setTeamName("???B ?????? ??????");

        teamA = teamRepository.saveAndFlush(teamA);
        teamB = teamRepository.saveAndFlush(teamB);

        System.out.println("------??? ?????? ?????? ??? ??????------");
        System.out.println(teamA.getTeamName());
        System.out.println(teamB.getTeamName());

    }
}
