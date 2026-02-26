package com.project.festmap.address.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.festmap.address.domain.Address;
import com.project.festmap.address.domain.AddressRepository;
import com.project.festmap.address.dto.AddressRequest;
import com.project.festmap.address.dto.AddressResponse;
import com.project.festmap.common.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService {

  private final AddressRepository addressRepository;

  @Transactional
  public Address createAddress(AddressRequest request) {
    Address address =
        Address.builder()
            .latitude(request.getLatitude())
            .longitude(request.getLongitude())
            .addressLine(request.getAddressLine())
            .postalCode(request.getPostalCode())
            .city(request.getCity())
            .country(request.getCountry())
            .build();
    return addressRepository.save(address);
  }

  @Transactional(readOnly = true)
  public List<AddressResponse> getAllAddresses() {
    return addressRepository.findAll().stream()
        .map(this::mapToAddressResponse)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public AddressResponse getAddressById(Long id) {
    Address address =
        addressRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
    return mapToAddressResponse(address);
  }

  @Transactional
  public AddressResponse updateAddress(Long id, AddressRequest request) {
    Address address =
        addressRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));

    address.setLatitude(request.getLatitude());
    address.setLongitude(request.getLongitude());
    address.setAddressLine(request.getAddressLine());
    address.setPostalCode(request.getPostalCode());
    address.setCity(request.getCity());
    address.setCountry(request.getCountry());

    Address updatedAddress = addressRepository.save(address);
    return mapToAddressResponse(updatedAddress);
  }

  @Transactional
  public void deleteAddress(Long id) {
    if (!addressRepository.existsById(id)) {
      throw new ResourceNotFoundException("Address not found with id: " + id);
    }
    addressRepository.deleteById(id);
  }

  public AddressResponse mapToAddressResponse(Address address) {
    return AddressResponse.builder()
        .id(address.getId())
        .latitude(address.getLatitude())
        .longitude(address.getLongitude())
        .addressLine(address.getAddressLine())
        .postalCode(address.getPostalCode())
        .city(address.getCity())
        .country(address.getCountry())
        .build();
  }
}
