package com.project.festmap.festival.dto;

import java.time.LocalDate;

import com.project.festmap.address.dto.AddressResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FestivalResponse {

  private Long id;
  private String name;
  private String description;
  private AddressResponse address;
  private LocalDate startDate;
  private LocalDate endDate;
  private String genre;
}
