package com.project.festmap.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.festmap.domain.Festival;
import com.project.festmap.repository.FestivalRepository;

@RestController
@RequestMapping("/api")
public class FestivalController {

  private final FestivalRepository festivalRepository;

  public FestivalController(FestivalRepository festivalRepository) {
    this.festivalRepository = festivalRepository;
  }

  @GetMapping("/festivals")
  public List<Festival> getAllFestivals() {
    return festivalRepository.findAll();
  }
}
