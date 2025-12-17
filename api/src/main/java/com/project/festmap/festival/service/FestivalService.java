package com.project.festmap.festival.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.project.festmap.festival.domain.Festival;
import com.project.festmap.festival.domain.FestivalRepository;
import com.project.festmap.festival.dto.FestivalRequest;
import com.project.festmap.festival.dto.FestivalResponse;
import com.project.festmap.festival.mapper.FestivalMapper;

@Service
public class FestivalService {

  private final FestivalRepository festivalRepository;
  private final FestivalMapper festivalMapper;

  public FestivalService(FestivalRepository festivalRepository, FestivalMapper festivalMapper) {
    this.festivalRepository = festivalRepository;
    this.festivalMapper = festivalMapper;
  }

  public List<FestivalResponse> getAllFestivals() {
    return festivalRepository.findAll().stream()
        .map(festivalMapper::toFestivalResponse)
        .collect(Collectors.toList());
  }

  public FestivalResponse createFestival(FestivalRequest festivalRequest) {
    Festival festival = festivalMapper.toFestivalEntity(festivalRequest);
    festival = festivalRepository.save(festival);
    return festivalMapper.toFestivalResponse(festival);
  }

  public void deleteFestival(Long id) {
    festivalRepository.deleteById(id);
  }
}
