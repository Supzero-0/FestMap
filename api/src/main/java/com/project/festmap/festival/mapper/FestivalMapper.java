package com.project.festmap.festival.mapper;

import org.springframework.stereotype.Component;

import com.project.festmap.address.domain.Address;
import com.project.festmap.address.dto.AddressResponse;
import com.project.festmap.festival.domain.Festival;
import com.project.festmap.festival.dto.FestivalRequest;
import com.project.festmap.festival.dto.FestivalResponse;

@Component
public class FestivalMapper {

  public FestivalResponse toFestivalResponse(Festival festival) {
    FestivalResponse festivalResponse = new FestivalResponse();
    festivalResponse.setId(festival.getId());
    festivalResponse.setName(festival.getName());
    festivalResponse.setDescription(festival.getDescription());
    festivalResponse.setStartDate(festival.getStartDate());
    festivalResponse.setEndDate(festival.getEndDate());
    festivalResponse.setGenre(festival.getGenre());

    if (festival.getAddress() != null) {
      Address address = festival.getAddress();
      festivalResponse.setAddress(
          AddressResponse.builder()
              .id(address.getId())
              .latitude(address.getLatitude())
              .longitude(address.getLongitude())
              .addressLine(address.getAddressLine())
              .postalCode(address.getPostalCode())
              .city(address.getCity())
              .country(address.getCountry())
              .build());
    }
    return festivalResponse;
  }

  public Festival toFestivalEntity(FestivalRequest festivalRequest) {
    Festival festival = new Festival();
    festival.setName(festivalRequest.getName());
    festival.setDescription(festivalRequest.getDescription());
    festival.setStartDate(festivalRequest.getStartDate());
    festival.setEndDate(festivalRequest.getEndDate());
    festival.setGenre(festivalRequest.getGenre());
    return festival;
  }
}
