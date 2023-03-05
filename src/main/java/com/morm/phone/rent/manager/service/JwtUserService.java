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
public class JwtUserService implements UserDetailsService {

  private final UserRepository userRepository;

  public JwtUserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    return userRepository.findByUsername(username)
        .map(user -> new User(user.getUsername(), user.getPassword(),
            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))))
        .orElseThrow(
            () -> new UsernameNotFoundException("User not found with username: " + username));
  }

  public void save(Employee user) {
    userRepository.save(user);
  }

  public boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }
}
