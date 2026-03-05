package com.project.festmap.user.dto;

import com.project.festmap.user.domain.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserRequest {

  @NotBlank(message = "L'email est obligatoire")
  @Email(message = "L'email doit être valide")
  String email;

  @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
  String password;

  @NotNull(message = "Le rôle est obligatoire")
  Role role;
}
