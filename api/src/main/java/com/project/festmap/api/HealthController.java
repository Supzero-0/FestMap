package com.project.festmap.api;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.festmap.infrastructure.db.DatabaseService;

@RestController
@RequestMapping("/api")
public class HealthController {
  private final DatabaseService db;

  public HealthController(DatabaseService db) {
    this.db = db;
  }

  @GetMapping("/health")
  public ResponseEntity<Map<String, String>> health() {
    boolean ok = db.ping();
    if (ok) {
      return ResponseEntity.ok(Map.of("status", "ok", "message", "API connected to database!"));
    }
    return ResponseEntity.status(503)
        .body(Map.of("status", "error", "message", "Database connection failed"));
  }
}
