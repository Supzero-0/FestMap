package com.project.festmap.festival.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class FestivalRequest {

  @NotBlank(message = "Le nom est obligatoire")
  @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
  private String name;

  @Size(max = 5000, message = "La description ne peut pas dépasser 5000 caractères")
  private String description;

  @Size(max = 255, message = "L'adresse ne peut pas dépasser 255 caractères")
  private String address;

  @NotBlank(message = "La ville est obligatoire")
  @Size(max = 100, message = "La ville ne peut pas dépasser 100 caractères")
  private String city;

  @Pattern(regexp = "^[A-Za-z0-9\\s-]{2,10}$", message = "Le code postal est invalide")
  private String postalCode;

  @NotBlank(message = "Le pays est obligatoire")
  @Size(max = 100, message = "Le pays ne peut pas dépasser 100 caractères")
  private String country;

  @DecimalMin(
      value = "-90.0",
      inclusive = true,
      message = "La latitude doit être supérieure ou égale à -90")
  @DecimalMax(
      value = "90.0",
      inclusive = true,
      message = "La latitude doit être inférieure ou égale à 90")
  private double latitude;

  @DecimalMin(
      value = "-180.0",
      inclusive = true,
      message = "La longitude doit être supérieure ou égale à -180")
  @DecimalMax(
      value = "180.0",
      inclusive = true,
      message = "La longitude doit être inférieure ou égale à 180")
  private double longitude;

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

  // Getters and setters

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public String getGenre() {
    return genre;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }
}
