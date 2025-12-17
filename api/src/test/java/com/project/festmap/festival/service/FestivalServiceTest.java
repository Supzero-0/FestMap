package com.project.festmap.festival.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.festmap.festival.domain.Festival;
import com.project.festmap.festival.domain.FestivalRepository;
import com.project.festmap.festival.dto.FestivalResponse;
import com.project.festmap.festival.mapper.FestivalMapper;
import com.project.festmap.shared.exception.FestivalNotFoundException;

@ExtendWith(MockitoExtension.class)
public class FestivalServiceTest {

  @Mock private FestivalRepository festivalRepository;

  @Mock private FestivalMapper festivalMapper;

  @InjectMocks private FestivalService festivalService;

  @Test
  public void getFestivalById_whenFestivalExists_shouldReturnFestivalResponse() {
    // Arrange
    Long festivalId = 1L;
    Festival festival = new Festival();
    festival.setId(festivalId);
    festival.setName("Test Festival");

    FestivalResponse expectedResponse = new FestivalResponse();
    expectedResponse.setId(festivalId);
    expectedResponse.setName("Test Festival");

    when(festivalRepository.findById(festivalId)).thenReturn(Optional.of(festival));
    when(festivalMapper.toFestivalResponse(festival)).thenReturn(expectedResponse);

    // Act
    FestivalResponse actualResponse = festivalService.getFestivalById(festivalId);

    // Assert
    assertNotNull(actualResponse);
    assertEquals(expectedResponse.getId(), actualResponse.getId());
    assertEquals(expectedResponse.getName(), actualResponse.getName());
  }

  @Test
  public void getFestivalById_whenFestivalDoesNotExist_shouldThrowFestivalNotFoundException() {
    // Arrange
    Long festivalId = 2L;
    when(festivalRepository.findById(festivalId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        FestivalNotFoundException.class,
        () -> {
          festivalService.getFestivalById(festivalId);
        });
  }
}
