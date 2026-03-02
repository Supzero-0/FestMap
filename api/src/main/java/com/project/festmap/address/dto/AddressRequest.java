package com.project.festmap.address.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AddressRequest {

  @NotNull
  @DecimalMin(value = "-90.0")
  @DecimalMax(value = "90.0")
  Double latitude;

  @NotNull
  @DecimalMin(value = "-180.0")
  @DecimalMax(value = "180.0")
  Double longitude;

  @NotBlank
  @Size(max = 255)
  String addressLine;

  @NotBlank
  @Size(max = 10)
  String postalCode;

  @NotBlank
  @Size(max = 100)
  String city;

  @NotBlank
  @Size(max = 100)
  String country;
}
