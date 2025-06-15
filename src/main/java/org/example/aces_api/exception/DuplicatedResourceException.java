package org.example.aces_api.exception;

public class DuplicatedResourceException extends RuntimeException {
  public DuplicatedResourceException(String message) {
    super(message);
  }
}
