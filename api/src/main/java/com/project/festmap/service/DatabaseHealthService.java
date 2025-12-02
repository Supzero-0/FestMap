package com.project.festmap.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DatabaseHealthService {
  private final JdbcTemplate jdbc;

  public DatabaseHealthService(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public boolean ping() {
    Integer one = jdbc.queryForObject("SELECT 1", Integer.class);
    return one != null && one == 1;
  }
}
