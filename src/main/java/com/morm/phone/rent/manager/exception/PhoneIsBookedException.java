package com.morm.phone.rent.manager.exception;

public class PhoneIsBookedException extends RuntimeException {

  public PhoneIsBookedException(String name, Integer phoneId) {
    super(String.format("Phone %d is already booked by %s", phoneId, name));
  }
}
