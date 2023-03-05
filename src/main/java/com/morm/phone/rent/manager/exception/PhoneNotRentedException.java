package com.morm.phone.rent.manager.exception;

public class PhoneNotRentedException extends RuntimeException {

  public PhoneNotRentedException(String name, Integer phoneId) {
    super(String.format("Phone %d was not rented by %s", phoneId, name));
  }
}
