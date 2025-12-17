package com.project.festmap.shared.exception;

import java.util.stream.Collectors;

import org.springframework.dao.*;
import org.springframework.http.*;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // Validation
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    var details =
        ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.toList());
    var errorResponse = new ErrorResponse("Validation Failed", details);
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  // Festival not found
  @ExceptionHandler(FestivalNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleFestivalNotFound(FestivalNotFoundException ex) {
    var errorResponse = new ErrorResponse(ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  // Connexion/timeout/DB down -> 503
  @ExceptionHandler({
    DataAccessResourceFailureException.class,
    RecoverableDataAccessException.class,
    QueryTimeoutException.class,
    CannotGetJdbcConnectionException.class
  })
  public ResponseEntity<ErrorResponse> handleDbUnavailable(Exception ex) {
    var errorResponse = new ErrorResponse("Database unavailable");
    return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
  }

  // Conflit d’unicité (ex: clé unique) -> 409
  @ExceptionHandler(DuplicateKeyException.class)
  public ResponseEntity<ErrorResponse> handleDuplicate(Exception ex) {
    var errorResponse = new ErrorResponse("Duplicate resource");
    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  // Mauvaise requête SQL / paramètres invalides -> 400
  @ExceptionHandler({InvalidDataAccessApiUsageException.class, BadSqlGrammarException.class})
  public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex) {
    var errorResponse = new ErrorResponse("Bad request");
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  // Par défaut -> 500
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
    var errorResponse = new ErrorResponse("Unexpected server error");
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
