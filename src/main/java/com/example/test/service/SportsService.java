package com.example.test.service;

import com.example.test.model.Sports;
import com.example.test.repository.SportsRepository;
import com.example.test.service.dto.SportsDto;
import com.example.test.service.dto.TeamDto;
import com.example.test.service.dto.mapper.SportsDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SportsService {

    private final SportsRepository sportsRepository;

    public SportsDto getSports(Long sportsId) {
        Sports findSports = sportsRepository.findById(sportsId)
                .orElseThrow(RuntimeException::new);

        return SportsDtoMapper.instance.toDto(findSports);
    }


}
