package com.project.festmap.user.dto;

import com.project.festmap.user.domain.Role;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
  private String token;
  private String email;
  private Role role;
}
