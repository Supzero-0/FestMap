package com.project.festmap.infrastructure.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {
  private final JdbcTemplate jdbc;

  public DatabaseService(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public boolean ping() {
    Integer one = jdbc.queryForObject("SELECT 1", Integer.class);
    return one != null && one == 1;
  }
}
