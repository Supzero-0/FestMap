package com.project.festmap.infrastructure.db;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DatabaseConfig {

  @Bean
  public DataSource dataSource(
      @Value("${spring.datasource.url}") String url,
      @Value("${spring.datasource.username}") String username,
      @Value("${spring.datasource.password}") String password) {
    HikariConfig cfg = new HikariConfig();
    cfg.setJdbcUrl(url);
    cfg.setUsername(username);
    cfg.setPassword(password);
    cfg.setPoolName("FestMapHikari");
    cfg.setMaximumPoolSize(10);
    cfg.setConnectionTimeout(30_000);
    cfg.setValidationTimeout(5_000);
    cfg.setLeakDetectionThreshold(20_000);
    cfg.setInitializationFailTimeout(-1);
    cfg.setKeepaliveTime(60_000);
    cfg.setConnectionTestQuery("SELECT 1");
    return new HikariDataSource(cfg);
  }
}
