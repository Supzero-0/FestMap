package com.project.festmap.address.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "latitude", nullable = false)
  private Double latitude;

  @Column(name = "longitude", nullable = false)
  private Double longitude;

  @Column(name = "address_line", nullable = false)
  private String addressLine;

  @Column(name = "postal_code", nullable = false, length = 10)
  private String postalCode;

  @Column(name = "city", nullable = false, length = 100)
  private String city;

  @Column(name = "country", nullable = false, length = 100)
  private String country;
}
