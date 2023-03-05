package com.morm.phone.rent.manager.service;

import com.morm.phone.rent.manager.domain.Phone;
import com.morm.phone.rent.manager.domain.PhoneRent;
import com.morm.phone.rent.manager.exception.EmployeeNotFound;
import com.morm.phone.rent.manager.exception.PhoneNotRentedException;
import com.morm.phone.rent.manager.repository.PhoneRentRepository;
import com.morm.phone.rent.manager.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PhonesRentService {

  private final PhoneRentRepository phoneRentRepository;

  private final UserRepository userRepository;

  public PhonesRentService(PhoneRentRepository phoneRentRepository, UserRepository userRepository) {
    this.phoneRentRepository = phoneRentRepository;
    this.userRepository = userRepository;
  }


  public List<PhoneRent> getRentedPhones() {
    return phoneRentRepository.findAllWithCurrentState();
  }

  public void releasePhone(String name, Integer phoneId) {
    // Check if phone is rented by user, or else throw exception
    phoneRentRepository.findRentedPhoneByUser(name, phoneId).ifPresentOrElse(phoneRent -> {
      phoneRent.setReleasedAt(LocalDateTime.now());
      phoneRentRepository.save(phoneRent);
    }, () -> {
      throw new PhoneNotRentedException(name, phoneId);
    });
  }

  public void bookPhone(String name, Integer phoneId) {
    // Check if phone is available, or else throw exception
    phoneRentRepository.findRentedPhoneByUser(name, phoneId).ifPresentOrElse(phoneRent -> {
      throw new PhoneNotRentedException(name, phoneId);
    }, () -> {
      PhoneRent phoneForRent = new PhoneRent();
      phoneForRent.setPhone(new Phone(phoneId));
      userRepository.findByUsername(name).ifPresentOrElse(employee -> {
        phoneForRent.setEmployee(employee);
        phoneForRent.setBookedAt(LocalDateTime.now());
        phoneRentRepository.save(phoneForRent);
      }, () -> {
        throw new EmployeeNotFound(name);
      });
    });
  }
}
