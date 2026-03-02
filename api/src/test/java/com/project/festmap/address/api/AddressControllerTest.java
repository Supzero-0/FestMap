package com.project.festmap.address.api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.festmap.address.domain.Address;
import com.project.festmap.address.dto.AddressRequest;
import com.project.festmap.address.dto.AddressResponse;
import com.project.festmap.address.service.AddressService;
import com.project.festmap.common.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayName("AddressController Tests")
class AddressControllerTest {

  private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Mock private AddressService addressService;

  @InjectMocks private AddressController addressController;

  private Address addressEntity;
  private AddressRequest addressRequest;
  private AddressResponse addressResponse;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(addressController).build();

    addressEntity = new Address();
    addressEntity.setId(1L);
    addressEntity.setAddressLine("1 Test Street");
    addressEntity.setPostalCode("75000");
    addressEntity.setCity("Test City");
    addressEntity.setCountry("Test Country");
    addressEntity.setLatitude(45.0);
    addressEntity.setLongitude(5.0);

    addressRequest =
        AddressRequest.builder()
            .addressLine("1 Test Street")
            .postalCode("75000")
            .city("Test City")
            .country("Test Country")
            .latitude(45.0)
            .longitude(5.0)
            .build();

    addressResponse =
        AddressResponse.builder()
            .id(1L)
            .addressLine("1 Test Street")
            .postalCode("75000")
            .city("Test City")
            .country("Test Country")
            .latitude(45.0)
            .longitude(5.0)
            .build();
  }

  @Test
  @WithMockUser
  @DisplayName("POST /api/addresses - Créer une nouvelle adresse avec succès")
  void createAddress_shouldReturnCreatedAddress() throws Exception {
    // Arrange
    when(addressService.createAddress(any(AddressRequest.class))).thenReturn(addressEntity);
    when(addressService.mapToAddressResponse(any(Address.class))).thenReturn(addressResponse);

    // Act
    ResultActions result =
        mockMvc.perform(
            post("/api/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressRequest)));

    // Assert
    result
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(addressResponse.getId().intValue())))
        .andExpect(jsonPath("$.addressLine", is(addressResponse.getAddressLine())));
  }

  @Test
  @WithMockUser
  @DisplayName("POST /api/addresses - Retourne 400 si la requête de création est invalide")
  void createAddress_shouldReturnBadRequest_whenInvalidRequest() throws Exception {
    // Arrange
    AddressRequest invalidAddressRequest =
        AddressRequest.builder().addressLine("").postalCode("").build();

    // Act
    ResultActions result =
        mockMvc.perform(
            post("/api/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidAddressRequest)));

    // Assert
    result.andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("GET /api/addresses - Retourne toutes les adresses")
  void getAllAddresses_shouldReturnListOfAddresses() throws Exception {
    // Arrange
    List<AddressResponse> allAddresses =
        Arrays.asList(addressResponse, AddressResponse.builder().id(2L).city("Other City").build());
    when(addressService.getAllAddresses()).thenReturn(allAddresses);

    // Act
    ResultActions result =
        mockMvc.perform(get("/api/addresses").contentType(MediaType.APPLICATION_JSON));

    // Assert
    result
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].city", is(addressResponse.getCity())));
  }

  @Test
  @DisplayName("GET /api/addresses - Retourne une liste vide si aucune adresse n'existe")
  void getAllAddresses_shouldReturnEmptyList_whenNoAddressesExist() throws Exception {
    // Arrange
    when(addressService.getAllAddresses()).thenReturn(Collections.emptyList());

    // Act
    ResultActions result =
        mockMvc.perform(get("/api/addresses").contentType(MediaType.APPLICATION_JSON));

    // Assert
    result.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  @DisplayName("GET /api/addresses/{id} - Retourne une adresse existante")
  void getAddressById_shouldReturnExistingAddress() throws Exception {
    // Arrange
    when(addressService.getAddressById(eq(1L))).thenReturn(addressResponse);

    // Act
    ResultActions result =
        mockMvc.perform(get("/api/addresses/1").contentType(MediaType.APPLICATION_JSON));

    // Assert
    result
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(addressResponse.getId().intValue())))
        .andExpect(jsonPath("$.addressLine", is(addressResponse.getAddressLine())));
  }

  @Test
  @DisplayName("GET /api/addresses/{id} - Retourne 404 pour un ID inexistant")
  void getAddressById_shouldReturnNotFound_whenIdDoesNotExist() throws Exception {
    // Arrange
    when(addressService.getAddressById(eq(99L)))
        .thenThrow(new ResourceNotFoundException("Address not found with id: 99"));

    // Act
    ResultActions result =
        mockMvc.perform(get("/api/addresses/99").contentType(MediaType.APPLICATION_JSON));

    // Assert
    result.andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser
  @DisplayName("PUT /api/addresses/{id} - Met à jour une adresse existante avec succès")
  void updateAddress_shouldReturnUpdatedAddress() throws Exception {
    // Arrange
    AddressRequest updatedRequest =
        AddressRequest.builder()
            .addressLine("Updated Street")
            .postalCode("00000")
            .city("Updated City")
            .country("Updated Country")
            .latitude(46.0)
            .longitude(6.0)
            .build();

    AddressResponse updatedResponse =
        AddressResponse.builder()
            .id(1L)
            .addressLine("Updated Street")
            .postalCode("00000")
            .city("Updated City")
            .country("Updated Country")
            .latitude(46.0)
            .longitude(6.0)
            .build();

    when(addressService.updateAddress(eq(1L), any(AddressRequest.class)))
        .thenReturn(updatedResponse);

    // Act
    ResultActions result =
        mockMvc.perform(
            put("/api/addresses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRequest)));

    // Assert
    result
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(updatedResponse.getId().intValue())))
        .andExpect(jsonPath("$.addressLine", is(updatedResponse.getAddressLine())));
  }

  @Test
  @WithMockUser
  @DisplayName("PUT /api/addresses/{id} - Retourne 400 si la requête de mise à jour est invalide")
  void updateAddress_shouldReturnBadRequest_whenInvalidRequest() throws Exception {
    // Arrange
    AddressRequest invalidAddressRequest =
        AddressRequest.builder().addressLine("").postalCode("").build();

    // Act
    ResultActions result =
        mockMvc.perform(
            put("/api/addresses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidAddressRequest)));

    // Assert
    result.andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser
  @DisplayName("PUT /api/addresses/{id} - Retourne 404 pour un ID de mise à jour inexistant")
  void updateAddress_shouldReturnNotFound_whenIdDoesNotExist() throws Exception {
    // Arrange
    when(addressService.updateAddress(eq(99L), any(AddressRequest.class)))
        .thenThrow(new ResourceNotFoundException("Address not found with id: 99"));

    // Act
    ResultActions result =
        mockMvc.perform(
            put("/api/addresses/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressRequest)));

    // Assert
    result.andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser
  @DisplayName("DELETE /api/addresses/{id} - Supprime une adresse existante avec succès")
  void deleteAddress_shouldReturnNoContent() throws Exception {
    // Arrange
    doNothing().when(addressService).deleteAddress(eq(1L));

    // Act
    ResultActions result =
        mockMvc.perform(delete("/api/addresses/1").contentType(MediaType.APPLICATION_JSON));

    // Assert
    result.andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser
  @DisplayName("DELETE /api/addresses/{id} - Retourne 404 pour un ID de suppression inexistant")
  void deleteAddress_shouldReturnNotFound_whenIdDoesNotExist() throws Exception {
    // Arrange
    doThrow(new ResourceNotFoundException("Address not found with id: 99"))
        .when(addressService)
        .deleteAddress(eq(99L));

    // Act
    ResultActions result =
        mockMvc.perform(delete("/api/addresses/99").contentType(MediaType.APPLICATION_JSON));

    // Assert
    result.andExpect(status().isNotFound());
  }
}
