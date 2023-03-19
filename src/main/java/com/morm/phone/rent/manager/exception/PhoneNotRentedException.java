package com.morm.phone.rent.manager.exception;

public class PhoneNotRentedException extends RuntimeException {

  public PhoneNotRentedException(String name, Integer phoneId) {
    super(String.format("Phone %d couldn't be rented by %s", phoneId, name));
  }
}
