package com.morm.phone.rent.manager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.morm.phone.rent.manager.domain.Employee;
import com.morm.phone.rent.manager.repository.UserRepository;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
public class JwtUserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private JwtUserServiceImpl jwtUserService;

  private Employee testUser;

  @BeforeEach
  public void setUp() {
    testUser = new Employee();
    testUser.setUsername("testuser");
    testUser.setPassword("testpassword");
  }

  @Test
  public void loadUserByUsername_ReturnsUserDetails_WhenUserExists() {
    // given
    when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

    // when
    UserDetails userDetails = jwtUserService.loadUserByUsername("testuser");

    // then
    assertThat(userDetails).isNotNull();
    assertThat(userDetails.getUsername()).isEqualTo("testuser");
    assertThat(userDetails.getPassword()).isEqualTo("testpassword");
    SimpleGrantedAuthority role = new SimpleGrantedAuthority("ROLE_USER");
    assertThat(userDetails.getAuthorities()).isEqualTo(Collections.singleton(role));
  }

  @Test
  public void loadUserByUsername_ThrowsUsernameNotFoundException_WhenUserDoesNotExist() {
    // given
    when(userRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

    // when, then
    assertThatThrownBy(() -> jwtUserService.loadUserByUsername("nonexistentuser"))
        .isInstanceOf(UsernameNotFoundException.class)
        .hasMessage("User not found with username: nonexistentuser");
  }

  @Test
  public void save_CallsUserRepositorySaveMethod() {
    // when
    jwtUserService.save(testUser);

    // then
    verify(userRepository, times(1)).save(testUser);
  }

  @Test
  public void existsByUsername_ReturnsTrue_WhenUserExists() {
    // given
    when(userRepository.existsByUsername("testuser")).thenReturn(true);

    // when
    boolean exists = jwtUserService.existsByUsername("testuser");

    // then
    assertThat(exists).isTrue();
  }

  @Test
  public void existsByUsername_ReturnsFalse_WhenUserDoesNotExist() {
    // given
    when(userRepository.existsByUsername("nonexistentuser")).thenReturn(false);

    // when
    boolean exists = jwtUserService.existsByUsername("nonexistentuser");

    // then
    assertThat(exists).isFalse();
  }
}
