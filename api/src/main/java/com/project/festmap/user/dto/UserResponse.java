package com.project.festmap.user.dto;

import com.project.festmap.user.domain.Role;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserResponse {
  String id;
  String email;
  Role role;
}
