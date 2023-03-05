package com.morm.phone.rent.manager.util;

import com.morm.phone.rent.manager.domain.Employee;
import java.time.LocalDateTime;
import java.util.Random;

public class EntityHelper {

  private static final String[] FIRST_NAMES = {"John", "Jane", "Mark", "Mary", "Bob", "Alice"};
  private static final String[] LAST_NAMES = {"Doe", "Smith", "Jones", "Brown", "Taylor",
      "Johnson"};
  private static final String[] PHONE_NAMES = {
      "Samsung Galaxy S9", "Apple iPhone 13", "OnePlus 9", "Google Pixel 5", "Motorola Moto G Power"
  };

  private static final Random random = new Random();

  public static Employee createEmployee(String username) {
    Employee employee = new Employee();
    employee.setUsername(username);
    employee.setPassword("password");
    employee.setFirstName(randomElement(FIRST_NAMES));
    employee.setLastName(randomElement(LAST_NAMES));
    return employee;
  }


  private static <T> T randomElement(T[] array) {
    return array[random.nextInt(array.length)];
  }

  private static LocalDateTime randomLocalDateTime() {
    return LocalDateTime.now().minusDays(random.nextInt(30)).minusHours(random.nextInt(24));
  }
}
