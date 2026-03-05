package com.project.festmap.shared.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.project.festmap.user.domain.Role;
import com.project.festmap.user.domain.User;
import com.project.festmap.user.domain.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Value("${app.admin.password}")
  private String adminPassword;

  @Value("${app.user.password}")
  private String userPassword;

  @Override
  public void run(String... args) {
    createAdmin();
    createUser();
  }

  private void createAdmin() {
    if (!userRepository.existsByEmail("admin@festmap.com")) {
      User admin =
          User.builder()
              .email("admin@festmap.com")
              .password(passwordEncoder.encode(adminPassword))
              .role(Role.ADMIN)
              .build();
      userRepository.save(admin);
      log.info("Default admin user created: admin@festmap.com");
    }
  }

  private void createUser() {
    if (!userRepository.existsByEmail("user@festmap.com")) {
      User user =
          User.builder()
              .email("user@festmap.com")
              .password(passwordEncoder.encode(userPassword))
              .role(Role.USER)
              .build();
      userRepository.save(user);
      log.info("Default standard user created: user@festmap.com");
    }
  }
}
