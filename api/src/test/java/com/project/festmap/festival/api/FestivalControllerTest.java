package com.project.festmap.festival.api;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.festmap.address.dto.AddressRequest;
import com.project.festmap.festival.dto.FestivalRequest;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FestivalControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @Test
  void shouldGetAllFestivals() throws Exception {
    mockMvc
        .perform(get("/api/festivals").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(6)))
        .andExpect(jsonPath("$[*].name", hasItem("Nuits Sonores")));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void shouldCreateFestival() throws Exception {
    FestivalRequest festivalRequest = new FestivalRequest();
    festivalRequest.setName("Test Festival");
    festivalRequest.setDescription("A test festival");
    festivalRequest.setGenre("Rock");
    festivalRequest.setStartDate(LocalDate.now().plusDays(1));
    festivalRequest.setEndDate(LocalDate.now().plusDays(2));

    AddressRequest address =
        AddressRequest.builder()
            .addressLine("1 Test Street")
            .postalCode("75000")
            .city("Test City")
            .country("Test Country")
            .latitude(45.0)
            .longitude(5.0)
            .build();

    festivalRequest.setAddress(address);

    mockMvc
        .perform(
            post("/api/festivals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(festivalRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name", is("Test Festival")))
        .andExpect(jsonPath("$.address.city", is("Test City")))
        .andExpect(jsonPath("$.address.country", is("Test Country")))
        .andExpect(jsonPath("$.address.latitude", is(45.0)))
        .andExpect(jsonPath("$.address.longitude", is(5.0)));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void shouldReturnBadRequestWhenCreatingFestivalWithInvalidData() throws Exception {
    FestivalRequest festivalRequest = new FestivalRequest();
    festivalRequest.setStartDate(LocalDate.now().plusDays(1));
    festivalRequest.setEndDate(LocalDate.now().plusDays(2));

    AddressRequest address =
        AddressRequest.builder()
            .city("Test City")
            .country("Test Country")
            .latitude(45.0)
            .longitude(5.0)
            .build();
    festivalRequest.setAddress(address);

    mockMvc
        .perform(
            post("/api/festivals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(festivalRequest)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void shouldDeleteExistingFestival() throws Exception {
    String body =
        mockMvc
            .perform(get("/api/festivals").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    JsonNode root = objectMapper.readTree(body);
    long existingId = root.get(0).get("id").asLong();

    mockMvc.perform(delete("/api/festivals/" + existingId)).andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void shouldReturnNotFoundWhenDeletingNonExistingFestival() throws Exception {
    mockMvc.perform(delete("/api/festivals/999")).andExpect(status().isNotFound());
  }
}
