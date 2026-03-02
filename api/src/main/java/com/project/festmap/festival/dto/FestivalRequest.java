package com.project.festmap.festival.dto;

import java.time.LocalDate;

import com.project.festmap.address.dto.AddressRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FestivalRequest {

  @NotBlank(message = "Le nom est obligatoire")
  @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
  private String name;

  @Size(max = 5000, message = "La description ne peut pas dépasser 5000 caractères")
  private String description;

  @NotNull(message = "L'adresse est obligatoire")
  @Valid
  private AddressRequest address;

  @NotNull(message = "La date de début est obligatoire")
  @FutureOrPresent(message = "La date de début ne peut pas être dans le passé")
  private LocalDate startDate;

  @NotNull(message = "La date de fin est obligatoire")
  @FutureOrPresent(message = "La date de fin ne peut pas être dans le passé")
  private LocalDate endDate;

  @Size(max = 50, message = "Le genre ne peut pas dépasser 50 caractères")
  private String genre;

  @AssertTrue(message = "La date de fin doit être après ou égale à la date de début")
  public boolean isEndDateAfterStartDate() {
    if (startDate == null || endDate == null) {
      return true; // La validation de nullité est gérée par @NotNull
    }
    return !endDate.isBefore(startDate);
  }
}
