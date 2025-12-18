package com.project.festmap.festival.api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.festmap.festival.dto.FestivalRequest;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
// Reset the database before each test method to ensure test isolation
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FestivalControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void shouldGetAllFestivals() throws Exception {
    mockMvc
        .perform(get("/api/festivals").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(6)));
  }

  @Test
  void shouldCreateFestival() throws Exception {
    FestivalRequest festivalRequest = new FestivalRequest();
    festivalRequest.setName("Test Festival");
    festivalRequest.setCity("Test City");
    festivalRequest.setCountry("Test Country");
    festivalRequest.setStartDate(LocalDate.now());
    festivalRequest.setEndDate(LocalDate.now().plusDays(1));
    festivalRequest.setLatitude(45.0);
    festivalRequest.setLongitude(5.0);

    mockMvc
        .perform(
            post("/api/festivals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(festivalRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name", is("Test Festival")));
  }

  @Test
  void shouldReturnBadRequestWhenCreatingFestivalWithInvalidData() throws Exception {
    FestivalRequest festivalRequest = new FestivalRequest();
    // Name is missing, which is invalid
    festivalRequest.setCity("Test City");
    festivalRequest.setCountry("Test Country");
    festivalRequest.setStartDate(LocalDate.now());
    festivalRequest.setEndDate(LocalDate.now().plusDays(1));
    festivalRequest.setLatitude(45.0);
    festivalRequest.setLongitude(5.0);

    mockMvc
        .perform(
            post("/api/festivals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(festivalRequest)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldDeleteExistingFestival() throws Exception {
    mockMvc.perform(delete("/api/festivals/1")).andExpect(status().isNoContent());
  }

  @Test
  void shouldReturnNotFoundWhenDeletingNonExistingFestival() throws Exception {
    mockMvc.perform(delete("/api/festivals/999")).andExpect(status().isNotFound());
  }
}
