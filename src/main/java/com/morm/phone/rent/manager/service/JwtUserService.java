package com.morm.phone.rent.manager.service;

import com.morm.phone.rent.manager.domain.Employee;
import com.morm.phone.rent.manager.repository.UserRepository;
import java.util.Collections;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface JwtUserService extends UserDetailsService {


  public void save(Employee user);

  public boolean existsByUsername(String username);
}
