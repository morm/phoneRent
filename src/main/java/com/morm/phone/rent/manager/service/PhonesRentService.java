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
public interface PhonesRentService {

  public List<PhoneRent> getRentedPhones();

  public void releasePhone(String name, Integer phoneId);

  public void bookPhone(String name, Integer phoneId);
}
