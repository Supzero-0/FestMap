package com.project.festmap.address.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

import com.project.festmap.address.domain.Address;
import com.project.festmap.address.domain.AddressRepository;
import com.project.festmap.address.dto.AddressRequest;
import com.project.festmap.address.dto.AddressResponse;
import com.project.festmap.shared.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
@DisplayName("AddressService Tests")
public class AddressServiceTest {

  @Mock private AddressRepository addressRepository;

  @InjectMocks private AddressService addressService;

  private Address address;
  private AddressRequest addressRequest;
  private AddressResponse addressResponse;

  @BeforeEach
  void setUp() {
    address = new Address();
    address.setId(1L);
    address.setAddressLine("1 Test Street");
    address.setPostalCode("75000");
    address.setCity("Test City");
    address.setCountry("Test Country");
    address.setLatitude(45.0);
    address.setLongitude(5.0);

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
  @DisplayName("Création d'une adresse avec succès")
  void createAddress_whenValidRequest_shouldReturnAddress() {
    when(addressRepository.save(any(Address.class))).thenReturn(address);

    Address createdAddress = addressService.createAddress(addressRequest);

    assertNotNull(createdAddress);
    assertEquals(address.getAddressLine(), createdAddress.getAddressLine());
    verify(addressRepository, times(1)).save(any(Address.class));
  }

  @Test
  @DisplayName("Récupération de toutes les adresses")
  void getAllAddresses_shouldReturnAddressResponseList() {
    when(addressRepository.findAll()).thenReturn(Collections.singletonList(address));

    List<AddressResponse> addressResponses = addressService.getAllAddresses();

    assertNotNull(addressResponses);
    assertEquals(1, addressResponses.size());
    assertEquals(addressResponse.getAddressLine(), addressResponses.get(0).getAddressLine());
    verify(addressRepository, times(1)).findAll();
  }

  @Test
  @DisplayName("Récupération d'une adresse par ID existant")
  void getAddressById_whenAddressExists_shouldReturnAddressResponse() {
    when(addressRepository.findById(1L)).thenReturn(Optional.of(address));

    AddressResponse foundAddress = addressService.getAddressById(1L);

    assertNotNull(foundAddress);
    assertEquals(addressResponse.getId(), foundAddress.getId());
    verify(addressRepository, times(1)).findById(1L);
  }

  @Test
  @DisplayName("Récupération d'une adresse par ID inexistant")
  void getAddressById_whenAddressDoesNotExist_shouldThrowResourceNotFoundException() {
    when(addressRepository.findById(2L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> addressService.getAddressById(2L));
    verify(addressRepository, times(1)).findById(2L);
  }

  @Test
  @DisplayName("Mise à jour d'une adresse existante")
  void updateAddress_whenAddressExists_shouldReturnUpdatedAddressResponse() {
    Address updatedAddress = new Address();
    updatedAddress.setId(1L);
    updatedAddress.setAddressLine("2 New Street");
    updatedAddress.setPostalCode("75001");
    updatedAddress.setCity("New City");
    updatedAddress.setCountry("New Country");
    updatedAddress.setLatitude(46.0);
    updatedAddress.setLongitude(6.0);

    AddressRequest updatedRequest =
        AddressRequest.builder()
            .addressLine("2 New Street")
            .postalCode("75001")
            .city("New City")
            .country("New Country")
            .latitude(46.0)
            .longitude(6.0)
            .build();

    when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
    when(addressRepository.save(any(Address.class))).thenReturn(updatedAddress);

    AddressResponse result = addressService.updateAddress(1L, updatedRequest);

    assertNotNull(result);
    assertEquals(updatedAddress.getAddressLine(), result.getAddressLine());
    assertEquals(updatedAddress.getCity(), result.getCity());
    verify(addressRepository, times(1)).findById(1L);
    verify(addressRepository, times(1)).save(any(Address.class));
  }

  @Test
  @DisplayName("Mise à jour d'une adresse inexistante")
  void updateAddress_whenAddressDoesNotExist_shouldThrowResourceNotFoundException() {
    when(addressRepository.findById(2L)).thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class, () -> addressService.updateAddress(2L, addressRequest));
    verify(addressRepository, times(1)).findById(2L);
  }

  @Test
  @DisplayName("Suppression d'une adresse existante")
  void deleteAddress_whenAddressExists_shouldDeleteAddress() {
    when(addressRepository.existsById(1L)).thenReturn(true);
    doNothing().when(addressRepository).deleteById(1L);

    addressService.deleteAddress(1L);

    verify(addressRepository, times(1)).existsById(1L);
    verify(addressRepository, times(1)).deleteById(1L);
  }

  @Test
  @DisplayName("Suppression d'une adresse inexistante")
  void deleteAddress_whenAddressDoesNotExist_shouldThrowResourceNotFoundException() {
    when(addressRepository.existsById(2L)).thenReturn(false);

    assertThrows(ResourceNotFoundException.class, () -> addressService.deleteAddress(2L));
    verify(addressRepository, times(1)).existsById(2L);
  }
}
