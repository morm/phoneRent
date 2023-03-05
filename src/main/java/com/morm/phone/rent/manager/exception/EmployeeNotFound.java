package com.morm.phone.rent.manager.exception;

public class EmployeeNotFound extends RuntimeException {

  public EmployeeNotFound(String name) {
    super(String.format("Employee %s not found", name));
  }
}
