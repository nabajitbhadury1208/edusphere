package com.cts.edusphere.exceptions.genericexceptions;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String mssg) {
    super(mssg);
  }
}
