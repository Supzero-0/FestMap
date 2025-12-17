package com.project.festmap.shared.exception;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

  private final String status = "error";
  private final String message;
  private final String timestamp;
  private final List<String> details;

  public ErrorResponse(String message, List<String> details) {
    this.message = message;
    this.details = details;
    this.timestamp = Instant.now().toString();
  }

  public ErrorResponse(String message) {
    this(message, null);
  }

  public String getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public List<String> getDetails() {
    return details;
  }
}
