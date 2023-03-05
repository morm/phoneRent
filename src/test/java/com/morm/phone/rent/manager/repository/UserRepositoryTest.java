package com.morm.phone.rent.manager.repository;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.morm.phone.rent.manager.configuration.DaoConfig;
import com.morm.phone.rent.manager.domain.Employee;
import com.morm.phone.rent.manager.util.EntityHelper;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ContextConfiguration(classes = {DaoConfig.class})
@Transactional("transactionManager")
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private SessionFactory sessionFactory;

  @Test
  @Rollback
  void testFindByUsername() {

    Employee employee = EntityHelper.createEmployee("jdoe");
    sessionFactory.getCurrentSession().save(employee);
    sessionFactory.getCurrentSession().flush();
    sessionFactory.getCurrentSession().clear();

    Optional<Employee> res = userRepository.findByUsername("jdoe");
    assertTrue(res.isPresent());
    assertEquals(employee.getFirstName(), res.get().getFirstName());
    assertEquals(employee.getLastName(), res.get().getLastName());
  }

  @Test
  @Rollback
  void testExistsByUsername() {
    Employee employee = EntityHelper.createEmployee("jdoe");
    sessionFactory.getCurrentSession().save(employee);
    sessionFactory.getCurrentSession().flush();
    sessionFactory.getCurrentSession().clear();

    assertTrue(userRepository.existsByUsername("jdoe"));
    assertFalse(userRepository.existsByUsername("nonexistentuser"));
  }

  @Test
  @Rollback
  void testSave() {
    Employee employee = new Employee();
    employee.setUsername("newuser");
    employee.setPassword(passwordEncoder.encode("newpassword"));
    employee.setFirstName("New");
    employee.setLastName("User");

    Employee savedEmployee = userRepository.save(employee);
    assertNotNull(savedEmployee.getId());

    Optional<Employee> retrievedEmployee = userRepository.findById(savedEmployee.getId());
    assertTrue(retrievedEmployee.isPresent());
    assertEquals("New", retrievedEmployee.get().getFirstName());
    assertEquals("User", retrievedEmployee.get().getLastName());
  }

  @Test
  @Rollback
  void whenUsernameDoesNotExist_thenReturnFalse() {

    Employee employee = EntityHelper.createEmployee("jdoe");
    sessionFactory.getCurrentSession().save(employee);
    sessionFactory.getCurrentSession().flush();
    sessionFactory.getCurrentSession().clear();

    boolean result = userRepository.existsByUsername("unknown");

    // Then
    assertFalse(result);
  }
}
