package com.project.festmap.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FestivalNotFoundException extends RuntimeException {

  public FestivalNotFoundException(String message) {
    super(message);
  }
}
