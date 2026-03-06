package com.project.festmap.festival.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.festmap.festival.dto.FestivalRequest;
import com.project.festmap.festival.dto.FestivalResponse;
import com.project.festmap.festival.service.FestivalService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class FestivalController {

  private final FestivalService festivalService;

  public FestivalController(FestivalService festivalService) {
    this.festivalService = festivalService;
  }

  @GetMapping("/festivals")
  public List<FestivalResponse> getAllFestivals() {
    return festivalService.getAllFestivals();
  }

  @GetMapping("/festivals/{id}")
  public ResponseEntity<FestivalResponse> getFestivalById(@PathVariable Long id) {
    FestivalResponse festivalResponse = festivalService.getFestivalById(id);
    return ResponseEntity.ok(festivalResponse);
  }

  @PostMapping("/festivals")
  public ResponseEntity<FestivalResponse> createFestival(
      @Valid @RequestBody FestivalRequest festivalRequest) {
    FestivalResponse festivalResponse = festivalService.createFestival(festivalRequest);
    return new ResponseEntity<>(festivalResponse, HttpStatus.CREATED);
  }

  @PutMapping("/festivals/{id}")
  public ResponseEntity<FestivalResponse> updateFestival(
      @PathVariable Long id, @Valid @RequestBody FestivalRequest festivalRequest) {
    FestivalResponse festivalResponse = festivalService.updateFestival(id, festivalRequest);
    return ResponseEntity.ok(festivalResponse);
  }

  @DeleteMapping("/festivals/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteFestival(@PathVariable Long id) {
    festivalService.deleteFestival(id);
  }

  @PostMapping("/festivals/{id}/favorite")
  public ResponseEntity<Boolean> toggleFavorite(@PathVariable Long id) {
    boolean isFavorite = festivalService.toggleFavorite(id);
    return ResponseEntity.ok(isFavorite);
  }
}
