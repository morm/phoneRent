package com.morm.phone.rent.manager.repository;

import com.morm.phone.rent.manager.configuration.DaoConfig;
import com.morm.phone.rent.manager.domain.Employee;
import com.morm.phone.rent.manager.domain.Phone;
import com.morm.phone.rent.manager.domain.PhoneRent;
import com.morm.phone.rent.manager.util.EntityHelper;
import java.util.List;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ContextConfiguration(classes = {DaoConfig.class})
@Transactional("transactionManager")
public class PhoneRentRepositoryTest {

  @Autowired
  private PhoneRentRepository phoneRentRepository;

  @Autowired
  private SessionFactory sessionFactory;

  @Test
  public void testFindAllWithCurrentState() {

    Employee employee = EntityHelper.createEmployee("testuser");
    Phone phone1 = EntityHelper.createPhone();
    Phone phone2 = EntityHelper.createPhone();
    Phone phone3 = EntityHelper.createPhone();
    sessionFactory.getCurrentSession().save(employee);
    sessionFactory.getCurrentSession().save(phone1);
    sessionFactory.getCurrentSession().save(phone2);
    sessionFactory.getCurrentSession().save(phone3);
    PhoneRent phoneRent1 = EntityHelper.createPhoneRent(employee, phone1, true);
    PhoneRent phoneRent2 = EntityHelper.createPhoneRent(employee, phone2, false);

    sessionFactory.getCurrentSession().save(phoneRent1);
    sessionFactory.getCurrentSession().save(phoneRent2);
    sessionFactory.getCurrentSession().flush();
    sessionFactory.getCurrentSession().clear();

    List<PhoneRent> phoneRents = phoneRentRepository.findAllWithCurrentState();
    Assertions.assertEquals(3, phoneRents.size());

    PhoneRent actualPhoneRent1 = phoneRents.get(0);
    Assertions.assertNull(actualPhoneRent1.getEmployee());
    Assertions.assertEquals(phone1.getId(), actualPhoneRent1.getPhone().getId());
    Assertions.assertNull(actualPhoneRent1.getBookedAt());
    Assertions.assertNull(actualPhoneRent1.getReleasedAt());
    Assertions.assertTrue(actualPhoneRent1.getIsAvailable());

    PhoneRent actualPhoneRent2 = phoneRents.get(1);
    Assertions.assertNotNull(actualPhoneRent2.getEmployee());
    Assertions.assertEquals(phone2.getId(), actualPhoneRent2.getPhone().getId());
    Assertions.assertNotNull(actualPhoneRent2.getBookedAt());
    Assertions.assertNull(actualPhoneRent2.getReleasedAt());
    Assertions.assertFalse(actualPhoneRent2.getIsAvailable());

    PhoneRent actualPhoneRent3 = phoneRents.get(2);
    Assertions.assertNull(actualPhoneRent3.getEmployee());
    Assertions.assertEquals(phone3.getId(), actualPhoneRent3.getPhone().getId());
    Assertions.assertNull(actualPhoneRent3.getBookedAt());
    Assertions.assertNull(actualPhoneRent3.getReleasedAt());
    Assertions.assertTrue(actualPhoneRent3.getIsAvailable());
  }
}
