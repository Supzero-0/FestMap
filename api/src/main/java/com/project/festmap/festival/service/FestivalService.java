package com.project.festmap.festival.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.project.festmap.festival.domain.Festival;
import com.project.festmap.festival.domain.FestivalRepository;
import com.project.festmap.festival.dto.FestivalRequest;
import com.project.festmap.festival.dto.FestivalResponse;
import com.project.festmap.festival.mapper.FestivalMapper;
import com.project.festmap.shared.exception.FestivalNotFoundException;

@Service
public class FestivalService {

  private final FestivalRepository festivalRepository;
  private final FestivalMapper festivalMapper;

  public FestivalService(FestivalRepository festivalRepository, FestivalMapper festivalMapper) {
    this.festivalRepository = festivalRepository;
    this.festivalMapper = festivalMapper;
  }

  public FestivalResponse getFestivalById(Long id) {
    return festivalRepository
        .findById(id)
        .map(festivalMapper::toFestivalResponse)
        .orElseThrow(() -> new FestivalNotFoundException("Festival with id " + id + " not found."));
  }

  public List<FestivalResponse> getAllFestivals() {
    return festivalRepository.findAll().stream()
        .map(festivalMapper::toFestivalResponse)
        .collect(Collectors.toList());
  }

  public FestivalResponse createFestival(FestivalRequest festivalRequest) {
    // Renvoie des Exception pour les test mockito
    if (festivalRequest.getName() == null || festivalRequest.getName().isBlank()) {
      throw new IllegalArgumentException("Festival name cannot be null or empty.");
    }
    if (festivalRequest.getStartDate() != null
        && festivalRequest.getEndDate() != null
        && festivalRequest.getEndDate().isBefore(festivalRequest.getStartDate())) {
      throw new IllegalArgumentException("End date cannot be before start date.");
    }

    Festival festival = festivalMapper.toFestivalEntity(festivalRequest);
    festival = festivalRepository.save(festival);
    return festivalMapper.toFestivalResponse(festival);
  }

  public FestivalResponse updateFestival(Long id, FestivalRequest festivalRequest) {
    Festival festival =
        festivalRepository
            .findById(id)
            .orElseThrow(
                () -> new FestivalNotFoundException("Festival with id " + id + " not found."));

    festival.setName(festivalRequest.getName());
    festival.setCity(festivalRequest.getCity());
    festival.setStartDate(festivalRequest.getStartDate());
    festival.setEndDate(festivalRequest.getEndDate());
    festival.setLatitude(festivalRequest.getLatitude());
    festival.setLongitude(festivalRequest.getLongitude());

    festival = festivalRepository.save(festival);
    return festivalMapper.toFestivalResponse(festival);
  }

  public void deleteFestival(Long id) {
    if (!festivalRepository.existsById(id)) {
      throw new FestivalNotFoundException("Festival with id " + id + " not found.");
    }
    festivalRepository.deleteById(id);
  }
}
