package com.project.festmap.address.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AddressResponse {
  Long id;
  Double latitude;
  Double longitude;
  String addressLine;
  String postalCode;
  String city;
  String country;
}
