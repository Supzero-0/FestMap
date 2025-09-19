package com.project.festmap.api;

import java.time.Instant;
import java.util.Map;

import org.springframework.dao.*;
import org.springframework.http.*;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // Connexion/timeout/DB down -> 503
  @ExceptionHandler({
    DataAccessResourceFailureException.class,
    RecoverableDataAccessException.class,
    QueryTimeoutException.class,
    CannotGetJdbcConnectionException.class
  })
  public ResponseEntity<Map<String, Object>> handleDbUnavailable(Exception ex) {
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
        .body(error("Database unavailable", ex));
  }

  // Conflit d’unicité (ex: clé unique) -> 409
  @ExceptionHandler(DuplicateKeyException.class)
  public ResponseEntity<Map<String, Object>> handleDuplicate(Exception ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error("Duplicate resource", ex));
  }

  // Mauvaise requête SQL / paramètres invalides -> 400
  @ExceptionHandler({InvalidDataAccessApiUsageException.class, BadSqlGrammarException.class})
  public ResponseEntity<Map<String, Object>> handleBadRequest(Exception ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error("Bad request", ex));
  }

  // Par défaut -> 500
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(error("Unexpected server error", ex));
  }

  private Map<String, Object> error(String message, Exception ex) {
    return Map.of("status", "error", "message", message, "timestamp", Instant.now().toString());
  }
}
