package com.morm.phone.rent.manager.util;

import com.morm.phone.rent.manager.domain.Employee;
import com.morm.phone.rent.manager.domain.Phone;
import com.morm.phone.rent.manager.domain.PhoneRent;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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

  public static Phone createPhone() {
    Phone phone = new Phone();
    phone.setName(randomElement(PHONE_NAMES));
    return phone;
  }

  public static PhoneRent createPhoneRent(Employee employee, Phone phone, boolean isAvailable) {
    PhoneRent phoneRent = new PhoneRent();
    phoneRent.setEmployee(employee);
    phoneRent.setPhone(phone);
    LocalDateTime bookedAt = randomLocalDateTime();
    phoneRent.setBookedAt(bookedAt);
    if (isAvailable) {
      phoneRent.setReleasedAt(bookedAt.plusHours(random.nextInt(24)));
    }
    return phoneRent;
  }

  public static Set<PhoneRent> createPhoneRents(Employee employee, int count) {
    Set<PhoneRent> phoneRents = new HashSet<>();
    for (int i = 0; i < count; i++) {
      phoneRents.add(createPhoneRent(employee, createPhone(), random.nextInt(2) == 0));
    }
    return phoneRents;
  }

  private static <T> T randomElement(T[] array) {
    return array[random.nextInt(array.length)];
  }

  private static LocalDateTime randomLocalDateTime() {
    return LocalDateTime.now().minusDays(random.nextInt(30)).minusHours(random.nextInt(24));
  }
}
