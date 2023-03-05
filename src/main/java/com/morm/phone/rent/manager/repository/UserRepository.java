package com.morm.phone.rent.manager.repository;

import com.morm.phone.rent.manager.domain.Employee;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Employee, Integer> {

  Optional<Employee> findByUsername(String username);

  Boolean existsByUsername(String username);
}
