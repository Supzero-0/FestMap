package com.project.festmap.health;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthController {
  private final HealthService service;

  public HealthController(HealthService service) {
    this.service = service;
  }

  @GetMapping("/health")
  public Map<String, String> health() {
    return service.check();
  }
}
