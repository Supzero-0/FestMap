package com.project.festmap.festival.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.festmap.address.domain.Address;
import com.project.festmap.address.service.AddressService;
import com.project.festmap.festival.domain.Festival;
import com.project.festmap.festival.domain.FestivalRepository;
import com.project.festmap.festival.dto.FestivalRequest;
import com.project.festmap.festival.dto.FestivalResponse;
import com.project.festmap.festival.mapper.FestivalMapper;
import com.project.festmap.shared.exception.FestivalNotFoundException;
import com.project.festmap.user.domain.User;
import com.project.festmap.user.domain.UserRepository;
import com.project.festmap.user.service.UserService;

@Service
public class FestivalService {

  private final FestivalRepository festivalRepository;
  private final FestivalMapper festivalMapper;
  private final AddressService addressService;
  private final UserService userService;
  private final UserRepository userRepository;

  public FestivalService(
      FestivalRepository festivalRepository,
      FestivalMapper festivalMapper,
      AddressService addressService,
      UserService userService,
      UserRepository userRepository) {
    this.festivalRepository = festivalRepository;
    this.festivalMapper = festivalMapper;
    this.addressService = addressService;
    this.userService = userService;
    this.userRepository = userRepository;
  }

  @Transactional(readOnly = true)
  public FestivalResponse getFestivalById(Long id) {
    Festival festival =
        festivalRepository
            .findById(id)
            .orElseThrow(
                () -> new FestivalNotFoundException("Festival with id " + id + " not found."));

    FestivalResponse response = festivalMapper.toFestivalResponse(festival);
    User currentUser = userService.getCurrentUser();
    if (currentUser != null) {
      Set<Long> favoriteIds = userRepository.findFavoriteFestivalIdsByUserId(currentUser.getId());
      response.setFavorite(favoriteIds.contains(festival.getId()));
    }
    return response;
  }

  @Transactional(readOnly = true)
  public List<FestivalResponse> getAllFestivals() {
    List<Festival> festivals = festivalRepository.findAllWithAddress();
    User currentUser = userService.getCurrentUser();
    Set<Long> favoriteIds =
        currentUser != null
            ? userRepository.findFavoriteFestivalIdsByUserId(currentUser.getId())
            : Set.of();

    return festivals.stream()
        .map(
            f -> {
              FestivalResponse res = festivalMapper.toFestivalResponse(f);
              res.setFavorite(favoriteIds.contains(f.getId()));
              return res;
            })
        .collect(Collectors.toList());
  }

  @Transactional
  public boolean toggleFavorite(Long festivalId) {
    User user = userService.getCurrentUser();
    if (user == null) {
      throw new BadCredentialsException("User must be authenticated to favorite a festival.");
    }

    Festival festival =
        festivalRepository
            .findById(festivalId)
            .orElseThrow(
                () ->
                    new FestivalNotFoundException(
                        "Festival with id " + festivalId + " not found."));

    boolean isFavorite;
    if (user.getFavoriteFestivals().contains(festival)) {
      user.getFavoriteFestivals().remove(festival);
      isFavorite = false;
    } else {
      user.getFavoriteFestivals().add(festival);
      isFavorite = true;
    }
    userRepository.save(user);
    return isFavorite;
  }

  @Transactional
  public FestivalResponse createFestival(FestivalRequest festivalRequest) {
    if (festivalRequest.getName() == null || festivalRequest.getName().isBlank()) {
      throw new IllegalArgumentException("Festival name cannot be null or empty.");
    }
    if (festivalRequest.getStartDate() != null
        && festivalRequest.getEndDate() != null
        && festivalRequest.getEndDate().isBefore(festivalRequest.getStartDate())) {
      throw new IllegalArgumentException("End date cannot be before start date.");
    }

    Address address = addressService.createAddress(festivalRequest.getAddress());

    Festival festival = festivalMapper.toFestivalEntity(festivalRequest);
    festival.setAddress(address);
    festival = festivalRepository.save(festival);
    return festivalMapper.toFestivalResponse(festival);
  }

  public FestivalResponse updateFestival(Long id, FestivalRequest festivalRequest) {
    Festival festival =
        festivalRepository
            .findById(id)
            .orElseThrow(
                () -> new FestivalNotFoundException("Festival with id " + id + " not found."));

    addressService.updateAddress(festival.getAddress().getId(), festivalRequest.getAddress());

    festival.setName(festivalRequest.getName());
    festival.setDescription(festivalRequest.getDescription());
    festival.setStartDate(festivalRequest.getStartDate());
    festival.setEndDate(festivalRequest.getEndDate());
    festival.setGenre(festivalRequest.getGenre());

    festival = festivalRepository.save(festival);
    return festivalMapper.toFestivalResponse(festival);
  }

  public void deleteFestival(Long id) {
    Festival festival =
        festivalRepository
            .findById(id)
            .orElseThrow(
                () -> new FestivalNotFoundException("Festival with id " + id + " not found."));

    festivalRepository.delete(festival);
    addressService.deleteAddress(festival.getAddress().getId());
  }
}
