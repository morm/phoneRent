package com.morm.phone.rent.manager.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.morm.phone.rent.manager.domain.Employee;
import com.morm.phone.rent.manager.domain.Phone;
import com.morm.phone.rent.manager.domain.PhoneRent;
import com.morm.phone.rent.manager.exception.EmployeeNotFound;
import com.morm.phone.rent.manager.exception.PhoneNotRentedException;
import com.morm.phone.rent.manager.repository.PhoneRentRepository;
import com.morm.phone.rent.manager.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PhonesRentServiceTest {

  @Mock
  private PhoneRentRepository phoneRentRepository;
  @Mock
  private UserRepository userRepository;
  private PhonesRentService phonesRentService;

  @BeforeEach
  void setUp() {
    phonesRentService = new PhonesRentServiceImpl(phoneRentRepository, userRepository);
  }

  @Test
  void testGetRentedPhones() {
    // given
    List<PhoneRent> phoneRents = Collections.singletonList(new PhoneRent());
    given(phoneRentRepository.findAllWithCurrentState()).willReturn(phoneRents);

    // when
    List<PhoneRent> result = phonesRentService.getRentedPhones();

    // then
    Assertions.assertEquals(phoneRents, result);
  }

  @Test
  void testReleasePhoneWhenPhoneIsRented() {
    // given
    PhoneRent phoneRent = new PhoneRent();
    phoneRent.setEmployee(new Employee());
    given(phoneRentRepository.findRentedPhoneByUser(anyString(), anyInt())).willReturn(
        Optional.of(phoneRent));

    // when
    phonesRentService.releasePhone("testuser", 1);

    // then
    verify(phoneRentRepository, Mockito.times(1)).save(phoneRent);
    Assertions.assertNotNull(phoneRent.getReleasedAt());
  }

  @Test
  void testReleasePhoneWhenPhoneIsNotRented() {
    // given
    given(phoneRentRepository.findRentedPhoneByUser(anyString(), anyInt())).willReturn(
        Optional.empty());

    // when
    Assertions.assertThrows(PhoneNotRentedException.class,
        () -> phonesRentService.releasePhone("testuser", 1));
  }

  @Test
  void testBookPhoneWhenEmployeeExists() {
    // given
    given(userRepository.findByUsername(anyString())).willReturn(Optional.of(new Employee()));
    given(phoneRentRepository.findRentedPhoneByUser(anyString(), anyInt())).willReturn(
        Optional.empty());

    // when
    phonesRentService.bookPhone("testuser", 1);

    // then
    verify(phoneRentRepository, Mockito.times(1)).save(any(PhoneRent.class));
  }

  @Test
  void testBookPhoneWhenEmployeeDoesNotExist() {
    // Arrange
    String employeeName = "JohnDoe";
    Integer phoneId = 1;

    given(userRepository.findByUsername(employeeName)).willReturn(Optional.empty());

    // Act & Assert
    assertThrows(EmployeeNotFound.class, () -> phonesRentService.bookPhone(employeeName, phoneId));
  }

  @Test
  void testBookPhoneWhenPhoneIsNotRented() {
    String name = "testuser";
    Integer phoneId = 1;
    PhoneRent phoneRent = new PhoneRent();
    phoneRent.setEmployee(new Employee());
    phoneRent.setPhone(new Phone());

    given(phoneRentRepository.findRentedPhoneByUser(name, phoneId))
        .willReturn(Optional.of(phoneRent));

    assertThrows(PhoneNotRentedException.class, () -> phonesRentService.bookPhone(name, phoneId));

    verify(phoneRentRepository, times(1)).findRentedPhoneByUser(name, phoneId);
    verify(phoneRentRepository, never()).save(any(PhoneRent.class));
  }
}