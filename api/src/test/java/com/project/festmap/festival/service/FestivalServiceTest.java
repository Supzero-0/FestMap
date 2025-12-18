package com.project.festmap.festival.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.festmap.festival.domain.Festival;
import com.project.festmap.festival.domain.FestivalRepository;
import com.project.festmap.festival.dto.FestivalRequest;
import com.project.festmap.festival.dto.FestivalResponse;
import com.project.festmap.festival.mapper.FestivalMapper;
import com.project.festmap.shared.exception.FestivalNotFoundException;

@ExtendWith(MockitoExtension.class)
@DisplayName("FestivalService Tests")
public class FestivalServiceTest {

  @Mock private FestivalRepository festivalRepository;

  @Mock private FestivalMapper festivalMapper;

  @InjectMocks private FestivalService festivalService;

  private Festival festival;
  private FestivalRequest festivalRequest;
  private FestivalResponse festivalResponse;

  @BeforeEach
  void setUp() {
    festival = new Festival();
    festival.setId(1L);
    festival.setName("Test Festival");

    festivalRequest = new FestivalRequest();
    festivalRequest.setName("Test Festival");

    festivalResponse = new FestivalResponse();
    festivalResponse.setId(1L);
    festivalResponse.setName("Test Festival");
  }

  @Test
  @DisplayName("Création d'un festival avec succès")
  public void createFestival_whenValidRequest_shouldReturnFestivalResponse() {
    // Arrange
    when(festivalMapper.toFestivalEntity(any(FestivalRequest.class))).thenReturn(festival);
    when(festivalRepository.save(any(Festival.class))).thenReturn(festival);
    when(festivalMapper.toFestivalResponse(any(Festival.class))).thenReturn(festivalResponse);

    // Act
    FestivalResponse actualResponse = festivalService.createFestival(festivalRequest);

    // Assert
    assertNotNull(actualResponse);
    assertEquals(festivalResponse.getName(), actualResponse.getName());
  }

  @Test
  @DisplayName("Récupération de tous les festivals")
  public void getAllFestivals_shouldReturnFestivalResponseList() {
    // Arrange
    List<Festival> festivals = Collections.singletonList(festival);
    when(festivalRepository.findAll()).thenReturn(festivals);
    when(festivalMapper.toFestivalResponse(any(Festival.class))).thenReturn(festivalResponse);

    // Act
    List<FestivalResponse> actualResponses = festivalService.getAllFestivals();

    // Assert
    assertNotNull(actualResponses);
    assertEquals(1, actualResponses.size());
    assertEquals(festivalResponse.getName(), actualResponses.get(0).getName());
  }

  @Test
  @DisplayName("Mise à jour d'un festival existant")
  public void updateFestival_whenFestivalExists_shouldReturnUpdatedFestivalResponse() {
    // Arrange
    when(festivalRepository.findById(1L)).thenReturn(Optional.of(festival));
    when(festivalRepository.save(any(Festival.class))).thenReturn(festival);
    when(festivalMapper.toFestivalResponse(any(Festival.class))).thenReturn(festivalResponse);

    // Act
    FestivalResponse actualResponse = festivalService.updateFestival(1L, festivalRequest);

    // Assert
    assertNotNull(actualResponse);
    assertEquals(festivalResponse.getName(), actualResponse.getName());
  }

  @Test
  @DisplayName("Mise à jour d'un festival inexistant")
  public void updateFestival_whenFestivalDoesNotExist_shouldThrowFestivalNotFoundException() {
    // Arrange
    when(festivalRepository.findById(2L)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        FestivalNotFoundException.class,
        () -> {
          festivalService.updateFestival(2L, festivalRequest);
        });
  }

  @Test
  @DisplayName("Suppression d'un festival existant")
  public void deleteFestival_whenFestivalExists_shouldDeleteFestival() {
    // Arrange
    when(festivalRepository.existsById(1L)).thenReturn(true);

    // Act
    festivalService.deleteFestival(1L);

    // Assert
    // No exception thrown
  }

  @Test
  @DisplayName("Suppression d'un festival inexistant")
  public void deleteFestival_whenFestivalDoesNotExist_shouldThrowFestivalNotFoundException() {
    // Arrange
    when(festivalRepository.existsById(2L)).thenReturn(false);

    // Act & Assert
    assertThrows(
        FestivalNotFoundException.class,
        () -> {
          festivalService.deleteFestival(2L);
        });
  }

  @Test
  @DisplayName("Création d'un festival avec un nom nul")
  public void createFestival_whenNameIsNull_shouldThrowException() {
    // Arrange
    festivalRequest.setName(null);

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          festivalService.createFestival(festivalRequest);
        });
  }

  @Test
  @DisplayName("Création d'un festival avec une date de fin antérieure à la date de début")
  public void createFestival_whenEndDateIsBeforeStartDate_shouldThrowException() {
    // Arrange
    festivalRequest.setStartDate(java.time.LocalDate.now());
    festivalRequest.setEndDate(java.time.LocalDate.now().minusDays(1));

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          festivalService.createFestival(festivalRequest);
        });
  }

  @Test
  @DisplayName("Récupération d'un festival par ID existant")
  public void getFestivalById_whenFestivalExists_shouldReturnFestivalResponse() {
    // Arrange
    when(festivalRepository.findById(1L)).thenReturn(Optional.of(festival));
    when(festivalMapper.toFestivalResponse(festival)).thenReturn(festivalResponse);

    // Act
    FestivalResponse actualResponse = festivalService.getFestivalById(1L);

    // Assert
    assertNotNull(actualResponse);
    assertEquals(festivalResponse.getId(), actualResponse.getId());
    assertEquals(festivalResponse.getName(), actualResponse.getName());
  }

  @Test
  @DisplayName("Récupération d'un festival par ID inexistant")
  public void getFestivalById_whenFestivalDoesNotExist_shouldThrowFestivalNotFoundException() {
    // Arrange
    when(festivalRepository.findById(2L)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        FestivalNotFoundException.class,
        () -> {
          festivalService.getFestivalById(2L);
        });
  }
}
