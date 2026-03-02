package com.project.festmap.address.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.festmap.address.domain.Address;
import com.project.festmap.address.dto.AddressRequest;
import com.project.festmap.address.dto.AddressResponse;
import com.project.festmap.address.service.AddressService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

  private final AddressService addressService;

  @PostMapping
  public ResponseEntity<AddressResponse> createAddress(@Valid @RequestBody AddressRequest request) {
    Address createdAddressEntity = addressService.createAddress(request);
    return new ResponseEntity<>(
        addressService.mapToAddressResponse(createdAddressEntity), HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<AddressResponse>> getAllAddresses() {
    List<AddressResponse> addresses = addressService.getAllAddresses();
    return ResponseEntity.ok(addresses);
  }

  @GetMapping("/{id}")
  public ResponseEntity<AddressResponse> getAddressById(@PathVariable Long id) {
    AddressResponse address = addressService.getAddressById(id);
    return ResponseEntity.ok(address);
  }

  @PutMapping("/{id}")
  public ResponseEntity<AddressResponse> updateAddress(
      @PathVariable Long id, @Valid @RequestBody AddressRequest request) {
    AddressResponse updatedAddress = addressService.updateAddress(id, request);
    return ResponseEntity.ok(updatedAddress);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
    addressService.deleteAddress(id);
    return ResponseEntity.noContent().build();
  }
}
