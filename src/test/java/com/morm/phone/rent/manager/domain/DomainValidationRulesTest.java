package com.morm.phone.rent.manager.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.morm.phone.rent.manager.configuration.DaoConfig;
import com.morm.phone.rent.manager.util.EntityHelper;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ContextConfiguration(classes = {DaoConfig.class})
@Transactional("transactionManager")
public class DomainValidationRulesTest {

  private Validator validator;

  @BeforeEach
  public void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }
  @Test
  void testCreateEmployeeWithBlankUsername() {
    Employee employee = EntityHelper.createEmployee("");
    Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
    assertEquals(1, violations.size());
    assertTrue(violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals("username")));
  }

  @Test
  void testCreateEmployeeWithLongUsername() {
    Employee employee = EntityHelper.createEmployee("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
    Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
    assertEquals(1, violations.size());
    assertTrue(violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals("username")));
  }

  @Test
  void testCreateEmployeeWithBlankFirstName() {
    Employee employee = EntityHelper.createEmployee("testuser");
    employee.setFirstName("");
    Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
    assertEquals(1, violations.size());
    assertTrue(violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals("firstName")));
  }

  @Test
  void testCreateEmployeeWithLongFirstName() {
    Employee employee = EntityHelper.createEmployee("testuser");
    employee.setFirstName("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
    Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
    assertEquals(1, violations.size());
    assertTrue(violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals("firstName")));
  }

  @Test
  void testCreateEmployeeWithBlankLastName() {
    Employee employee = EntityHelper.createEmployee("testuser");
    employee.setLastName("");
    Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
    assertEquals(1, violations.size());
    assertTrue(violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals("lastName")));
  }

  @Test
  void testCreateEmployeeWithLongLastName() {
    Employee employee = EntityHelper.createEmployee("testuser");
    employee.setLastName("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
    Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
    assertEquals(1, violations.size());
    assertTrue(violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals("lastName")));
  }

}
