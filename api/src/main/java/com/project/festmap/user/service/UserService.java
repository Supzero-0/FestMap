package com.project.festmap.user.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.festmap.shared.exception.ResourceNotFoundException;
import com.project.festmap.user.domain.User;
import com.project.festmap.user.domain.UserRepository;
import com.project.festmap.user.dto.UserRequest;
import com.project.festmap.user.dto.UserResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional(readOnly = true)
  public List<UserResponse> getAllUsers() {
    return userRepository.findAll().stream()
        .map(this::mapToUserResponse)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public UserResponse getUserById(UUID id) {
    return userRepository
        .findById(id)
        .map(this::mapToUserResponse)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
  }

  @Transactional
  public UserResponse createUser(UserRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new IllegalArgumentException("Email already exists");
    }

    User user =
        User.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(request.getRole())
            .build();

    return mapToUserResponse(userRepository.save(user));
  }

  @Transactional
  public UserResponse updateUser(UUID id, UserRequest request) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

    user.setEmail(request.getEmail());
    user.setRole(request.getRole());

    if (request.getPassword() != null && !request.getPassword().isBlank()) {
      user.setPassword(passwordEncoder.encode(request.getPassword()));
    }

    return mapToUserResponse(userRepository.save(user));
  }

  @Transactional
  public void deleteUser(UUID id) {
    if (!userRepository.existsById(id)) {
      throw new ResourceNotFoundException("User not found with id: " + id);
    }
    userRepository.deleteById(id);
  }

  private UserResponse mapToUserResponse(User user) {
    return UserResponse.builder()
        .id(user.getId().toString())
        .email(user.getEmail())
        .role(user.getRole())
        .build();
  }
}
