package com.example.test.service;

import com.example.test.model.Sports;
import com.example.test.model.Team;
import com.example.test.repository.SportsRepository;
import com.example.test.repository.TeamRepository;
import com.example.test.service.dto.TeamDto;
import com.example.test.service.dto.mapper.TeamDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<TeamDto> getAllTeamsSameSports(Long teamId) {
        Sports sports = teamRepository.findById(teamId).get().getSports();
        List<Team> teamList = sportsRepository.findById(sports.getSportsId()).get().getTeamList();
        return TeamDtoMapper.instance.toDtoList(teamList);
    }

    @Transactional
    public TeamDto getTeam(Long id) {

        return TeamDto.toDto(teamRepository.findById(id).orElseThrow(RuntimeException::new));
    }

    @Transactional
    @Cacheable(value = "team", key = "#id")
    public TeamDto getTeamFetch(Long id) {
        return TeamDto.toDto(teamRepository.findByIdFetch(id).orElseThrow(RuntimeException::new));
    }

    public TeamDto createTeam(Team team) {
        return TeamDto.toDto(teamRepository.save(team));
    }

    @Transactional
    @CachePut(value = "team", key = "#id")
    public TeamDto updateTeam(Long id, TeamDto team) {
        Sports findSports = sportsRepository.findById(team.getSportsDto().getSportsId())
                .orElseThrow(RuntimeException::new);
        Team findTeam = teamRepository.findById(id)
                .orElseThrow(RuntimeException::new);
        findTeam.setTeamName(team.getTeamName());
        findTeam.setSports(findSports);
        return TeamDto.toDto(teamRepository.save(findTeam));
    }

    @CacheEvict(value = "team", key = "#id")
    public TeamDto deleteTeam(Long id) {
        Team findTeam = teamRepository.findById(id)
                .orElseThrow(RuntimeException::new);
        teamRepository.delete(findTeam);
        return TeamDto.toDto(findTeam);
    }

    public void prePersist() {
        Team teamA = new Team();
        Team teamB = Team.builder()
                .teamName("엄청난 팀")
                .build();

        teamA = teamRepository.saveAndFlush(teamA);
        teamB = teamRepository.saveAndFlush(teamB);

        System.out.println("------팀 저장 후 첫 출력------");
        System.out.println(teamA.getTeamName());
        System.out.println(teamB.getTeamName());

        teamA.setTeamName("팀A 이름 수정");
        teamB.setTeamName("팀B 이름 수정");

        teamA = teamRepository.saveAndFlush(teamA);
        teamB = teamRepository.saveAndFlush(teamB);

        System.out.println("------팀 이름 수정 후 출력------");
        System.out.println(teamA.getTeamName());
        System.out.println(teamB.getTeamName());

    }
}
