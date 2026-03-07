package com.cts.edusphere.exceptions.genericexceptions;

public class NoNotificationFoundWithUserId extends RuntimeException {

  public NoNotificationFoundWithUserId(String mssg) {
    super(mssg);
  }
}
