package com.project.festmap.festival.mapper;

import org.springframework.stereotype.Component;

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
    festivalResponse.setAddress(festival.getAddress());
    festivalResponse.setCity(festival.getCity());
    festivalResponse.setPostalCode(festival.getPostalCode());
    festivalResponse.setCountry(festival.getCountry());
    festivalResponse.setLatitude(festival.getLatitude());
    festivalResponse.setLongitude(festival.getLongitude());
    festivalResponse.setStartDate(festival.getStartDate());
    festivalResponse.setEndDate(festival.getEndDate());
    festivalResponse.setGenre(festival.getGenre());
    return festivalResponse;
  }

  public Festival toFestivalEntity(FestivalRequest festivalRequest) {
    Festival festival = new Festival();
    festival.setName(festivalRequest.getName());
    festival.setDescription(festivalRequest.getDescription());
    festival.setAddress(festivalRequest.getAddress());
    festival.setCity(festivalRequest.getCity());
    festival.setPostalCode(festivalRequest.getPostalCode());
    festival.setCountry(festivalRequest.getCountry());
    festival.setLatitude(festivalRequest.getLatitude());
    festival.setLongitude(festivalRequest.getLongitude());
    festival.setStartDate(festivalRequest.getStartDate());
    festival.setEndDate(festivalRequest.getEndDate());
    festival.setGenre(festivalRequest.getGenre());
    return festival;
  }
}
