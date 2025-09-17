package com.project.festmap.health;

import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class HealthService {
    private final JdbcTemplate jdbcTemplate;

    public HealthService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, String> check() {
        try {
            Integer one = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            if (one != null && one == 1) {
                return Map.of("status", "ok", "message", "API connected to database!");
            }
            return Map.of("status", "error", "message", "Unexpected DB result");
        } catch (Exception e) {
            return Map.of("status", "error", "message", "Database connection failed");
        }
    }
}
