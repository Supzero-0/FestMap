package com.project.festmap.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

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
    DataSource ds = new HikariDataSource(cfg);
    return ProxyDataSourceBuilder.create(ds)
        .name("festmap-ds")
        .multiline()
        .countQuery()
        .logQueryBySlf4j()
        .build();
  }
}
