package com.project.festmap.user.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.festmap.user.dto.LoginRequest;
import com.project.festmap.user.dto.RegisterRequest;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  // --- Registration Tests ---

  @Test
  void shouldRegisterSuccessfully() throws Exception {
    RegisterRequest registerRequest =
        RegisterRequest.builder().email("register-ok@example.com").password("password123").build();

    mockMvc
        .perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").isString());
  }

  @Test
  void shouldFailRegistrationWhenEmailExists() throws Exception {
    // First, register a user
    RegisterRequest registerRequest =
        RegisterRequest.builder()
            .email("register-conflict@example.com")
            .password("password123")
            .build();
    mockMvc.perform(
        post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerRequest)));

    // Then, try to register again with the same email
    mockMvc
        .perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
        .andExpect(status().isConflict());
  }

  @Test
  void shouldFailRegistrationWithInvalidData() throws Exception {
    RegisterRequest registerRequest =
        RegisterRequest.builder().email("not-an-email").password("short").build();

    mockMvc
        .perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
        .andExpect(status().isBadRequest());
  }

  // --- Login Tests ---

  @Test
  void shouldLoginSuccessfully() throws Exception {
    // First, register a user
    RegisterRequest registerRequest =
        RegisterRequest.builder().email("login-ok@example.com").password("password123").build();
    mockMvc.perform(
        post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerRequest)));

    // Then, try to login
    LoginRequest loginRequest =
        LoginRequest.builder().email("login-ok@example.com").password("password123").build();
    mockMvc
        .perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").isString());
  }

  @Test
  void shouldFailLoginWithInvalidCredentials() throws Exception {
    // First, register a user
    RegisterRequest registerRequest =
        RegisterRequest.builder().email("login-fail@example.com").password("password123").build();
    mockMvc.perform(
        post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerRequest)));

    // Then, try to login with wrong password
    LoginRequest loginRequest =
        LoginRequest.builder().email("login-fail@example.com").password("wrongpassword").build();
    mockMvc
        .perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isUnauthorized());
  }

  // --- Protected Endpoint Tests ---

  @Test
  void shouldFailAccessingProtectedEndpointWithoutToken() throws Exception {
    mockMvc
        .perform(post("/api/festivals").contentType(MediaType.APPLICATION_JSON).content("{}"))
        .andExpect(status().isForbidden()); // Spring Security returns 403 by default
  }

  @Test
  void shouldAccessProtectedEndpointWithValidToken() throws Exception {
    // 1. Register and get token
    RegisterRequest registerRequest =
        RegisterRequest.builder()
            .email("protected-access@example.com")
            .password("password123")
            .build();
    MvcResult result =
        mockMvc
            .perform(
                post("/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(status().isOk())
            .andReturn();

    String responseString = result.getResponse().getContentAsString();
    String token = objectMapper.readTree(responseString).get("token").asText();

    // 2. Try to access protected endpoint with token
    String festivalContent =
        "{\"name\":\"SecureFest\",\"city\":\"Protected City\",\"country\":\"Authland\",\"startDate\":\"2026-10-10\",\"endDate\":\"2026-10-11\",\"latitude\":10,\"longitude\":10}";
    mockMvc
        .perform(
            post("/api/festivals")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(festivalContent))
        .andExpect(status().isCreated());
  }
}
